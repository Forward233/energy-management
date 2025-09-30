package com.yunpower.datav.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableReferenceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * ShardingSphere 5.4.1 手动配置类
 * 读取 Nacos 中的 spring.shardingsphere.* 配置并创建数据源
 */
@Configuration
public class ShardingSphereConfig {

    /**
     * 创建 ShardingSphere 数据源
     * 只有配置了 spring.shardingsphere.datasource.names 时才创建
     * 
     * 注意：虽然工厂方法返回 DataSource 接口，但实际类型是 ShardingSphereDataSource
     * 通过 Bean 名称 "shardingSphereDataSource" 可以让其他组件通过类型注入
     */
    @Bean(name = "shardingSphereDataSource")
    @ConditionalOnProperty(prefix = "spring.shardingsphere.datasource", name = "names")
    public DataSource shardingSphereDataSource(ShardingSphereProperties properties) throws SQLException {
        // 1. 创建数据源 Map
        Map<String, DataSource> dataSourceMap = createDataSourceMap(properties);

        // 2. 创建分片规则配置
        Collection<RuleConfiguration> ruleConfigs = new ArrayList<>();
        ShardingRuleConfiguration shardingRuleConfig = createShardingRuleConfiguration(properties);
        ruleConfigs.add(shardingRuleConfig);

        // 3. 创建 Mode 配置（Standalone 模式，避免依赖外部注册中心）
        ModeConfiguration modeConfig = new ModeConfiguration(
                "Standalone",
                new StandalonePersistRepositoryConfiguration("JDBC", new Properties())
        );

        // 4. 创建属性配置
        Properties props = new Properties();
        props.setProperty("sql-show", properties.getProps().getOrDefault("sql-show", "true"));

        // 5. 使用工厂方法创建 ShardingSphere 数据源
        // 指定默认数据库名称为 logic_db（与 RefreshActualDataNodesAO 中的 schemaName 一致）
        return ShardingSphereDataSourceFactory.createDataSource("logic_db", modeConfig, dataSourceMap, ruleConfigs, props);
    }

    /**
     * 创建数据源 Map
     */
    private Map<String, DataSource> createDataSourceMap(ShardingSphereProperties properties) {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        properties.getDatasource().getDataSourceMap().forEach((name, dsProps) -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setDriverClassName(dsProps.getDriverClassName());
            ds.setJdbcUrl(dsProps.getJdbcUrl());
            ds.setUsername(dsProps.getUsername());
            ds.setPassword(dsProps.getPassword());
            dataSourceMap.put(name, ds);
        });

        return dataSourceMap;
    }

    /**
     * 创建分片规则配置
     */
    private ShardingRuleConfiguration createShardingRuleConfiguration(ShardingSphereProperties properties) {
        ShardingRuleConfiguration config = new ShardingRuleConfiguration();

        // 分片表配置
        properties.getRules().getSharding().getTables().forEach((tableName, tableConfig) -> {
            ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration(
                    tableName,
                    tableConfig.getActualDataNodes()
            );

            // 分表策略
            if (tableConfig.getTableStrategy() != null && tableConfig.getTableStrategy().getStandard() != null) {
                var standard = tableConfig.getTableStrategy().getStandard();
                tableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration(
                        standard.getShardingColumn(),
                        standard.getShardingAlgorithmName()
                ));
            }

            config.getTables().add(tableRuleConfig);
        });

        // 绑定表配置（5.4.1 使用 ShardingTableReferenceRuleConfiguration）
        if (properties.getRules().getSharding().getBindingTables() != null) {
            String bindingTablesStr = properties.getRules().getSharding().getBindingTables();
            ShardingTableReferenceRuleConfiguration referenceRuleConfig = 
                    new ShardingTableReferenceRuleConfiguration("binding_ref", bindingTablesStr);
            config.getBindingTableGroups().add(referenceRuleConfig);
        }

        // 分片算法配置
        properties.getRules().getSharding().getShardingAlgorithms().forEach((algoName, algoConfig) -> {
            config.getShardingAlgorithms().put(algoName, new AlgorithmConfiguration(
                    algoConfig.getType(),
                    algoConfig.getProps()
            ));
        });

        return config;
    }

    /**
     * 配置属性映射类
     */
    @Configuration
    @ConfigurationProperties(prefix = "spring.shardingsphere")
    public static class ShardingSphereProperties {
        private DatasourceWrapperProperties datasource = new DatasourceWrapperProperties();
        private RulesProperties rules = new RulesProperties();
        private Map<String, String> props = new HashMap<>();

        public DatasourceWrapperProperties getDatasource() {
            return datasource;
        }

        public void setDatasource(DatasourceWrapperProperties datasource) {
            this.datasource = datasource;
        }

        public RulesProperties getRules() {
            return rules;
        }

        public void setRules(RulesProperties rules) {
            this.rules = rules;
        }

        public Map<String, String> getProps() {
            return props;
        }

        public void setProps(Map<String, String> props) {
            this.props = props;
        }

        public static class DatasourceWrapperProperties {
            private String names;
            private Map<String, DataSourceProperties> dataSourceMap = new HashMap<>();

            public String getNames() {
                return names;
            }

            public void setNames(String names) {
                this.names = names;
            }

            // 使用 @ConfigurationProperties 的动态键绑定功能
            public Map<String, DataSourceProperties> getDataSourceMap() {
                return dataSourceMap;
            }

            public void setDataSourceMap(Map<String, DataSourceProperties> dataSourceMap) {
                this.dataSourceMap = dataSourceMap;
            }

            // 为了支持 ds0, ds1 等动态键，需要这个方法
            public void set(String key, DataSourceProperties value) {
                if (!"names".equals(key)) {
                    dataSourceMap.put(key, value);
                }
            }
        }

        public static class DataSourceProperties {
            private String type;
            private String driverClassName;
            private String jdbcUrl;
            private String username;
            private String password;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDriverClassName() {
                return driverClassName;
            }

            public void setDriverClassName(String driverClassName) {
                this.driverClassName = driverClassName;
            }

            public String getJdbcUrl() {
                return jdbcUrl;
            }

            public void setJdbcUrl(String jdbcUrl) {
                this.jdbcUrl = jdbcUrl;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

        public static class RulesProperties {
            private ShardingProperties sharding = new ShardingProperties();

            public ShardingProperties getSharding() {
                return sharding;
            }

            public void setSharding(ShardingProperties sharding) {
                this.sharding = sharding;
            }
        }

        public static class ShardingProperties {
            private Map<String, TableProperties> tables = new HashMap<>();
            private String bindingTables;
            private Map<String, AlgorithmProperties> shardingAlgorithms = new HashMap<>();

            public Map<String, TableProperties> getTables() {
                return tables;
            }

            public void setTables(Map<String, TableProperties> tables) {
                this.tables = tables;
            }

            public String getBindingTables() {
                return bindingTables;
            }

            public void setBindingTables(String bindingTables) {
                this.bindingTables = bindingTables;
            }

            public Map<String, AlgorithmProperties> getShardingAlgorithms() {
                return shardingAlgorithms;
            }

            public void setShardingAlgorithms(Map<String, AlgorithmProperties> shardingAlgorithms) {
                this.shardingAlgorithms = shardingAlgorithms;
            }
        }

        public static class TableProperties {
            private String actualDataNodes;
            private TableStrategyProperties tableStrategy;

            public String getActualDataNodes() {
                return actualDataNodes;
            }

            public void setActualDataNodes(String actualDataNodes) {
                this.actualDataNodes = actualDataNodes;
            }

            public TableStrategyProperties getTableStrategy() {
                return tableStrategy;
            }

            public void setTableStrategy(TableStrategyProperties tableStrategy) {
                this.tableStrategy = tableStrategy;
            }
        }

        public static class TableStrategyProperties {
            private StandardStrategyProperties standard;

            public StandardStrategyProperties getStandard() {
                return standard;
            }

            public void setStandard(StandardStrategyProperties standard) {
                this.standard = standard;
            }
        }

        public static class StandardStrategyProperties {
            private String shardingColumn;
            private String shardingAlgorithmName;

            public String getShardingColumn() {
                return shardingColumn;
            }

            public void setShardingColumn(String shardingColumn) {
                this.shardingColumn = shardingColumn;
            }

            public String getShardingAlgorithmName() {
                return shardingAlgorithmName;
            }

            public void setShardingAlgorithmName(String shardingAlgorithmName) {
                this.shardingAlgorithmName = shardingAlgorithmName;
            }
        }

        public static class AlgorithmProperties {
            private String type;
            private Properties props = new Properties();

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Properties getProps() {
                return props;
            }

            public void setProps(Properties props) {
                this.props = props;
            }
        }
    }
}
