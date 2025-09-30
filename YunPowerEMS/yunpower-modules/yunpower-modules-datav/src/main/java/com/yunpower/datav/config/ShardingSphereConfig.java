package com.yunpower.datav.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableReferenceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * ShardingSphere 5.4.1 手动配置类
 * 从 Nacos Environment 中直接读取 spring.shardingsphere.* 配置并创建数据源
 */
@Configuration
public class ShardingSphereConfig {

    @Autowired
    private Environment env;

    /**
     * 创建 ShardingSphere 数据源
     */
    @Bean(name = "shardingSphereDataSource")
    @ConditionalOnProperty(prefix = "spring.shardingsphere.datasource", name = "names")
    public DataSource shardingSphereDataSource() throws SQLException {
        // 1. 读取数据源配置
        String names = env.getProperty("spring.shardingsphere.datasource.names");
        if (names == null || names.isEmpty()) {
            throw new IllegalArgumentException("spring.shardingsphere.datasource.names 配置为空");
        }

        // 2. 创建数据源 Map
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        String[] dsNames = names.split(",");
        
        for (String dsName : dsNames) {
            String prefix = "spring.shardingsphere.datasource." + dsName.trim() + ".";
            
            HikariDataSource ds = new HikariDataSource();
            ds.setDriverClassName(env.getProperty(prefix + "driver-class-name"));
            ds.setJdbcUrl(env.getProperty(prefix + "jdbc-url"));
            ds.setUsername(env.getProperty(prefix + "username"));
            ds.setPassword(env.getProperty(prefix + "password"));
            
            dataSourceMap.put(dsName.trim(), ds);
        }

        // 3. 创建分片规则配置
        Collection<RuleConfiguration> ruleConfigs = new ArrayList<>();
        ShardingRuleConfiguration shardingRuleConfig = createShardingRuleConfiguration();
        ruleConfigs.add(shardingRuleConfig);

        // 4. 创建 Mode 配置（Standalone 模式）
        ModeConfiguration modeConfig = new ModeConfiguration(
                "Standalone",
                new StandalonePersistRepositoryConfiguration("JDBC", new Properties())
        );

        // 5. 创建属性配置
        Properties props = new Properties();
        props.setProperty("sql-show", env.getProperty("spring.shardingsphere.props.sql-show", "true"));

        // 6. 使用工厂方法创建 ShardingSphere 数据源
        return ShardingSphereDataSourceFactory.createDataSource("logic_db", modeConfig, dataSourceMap, ruleConfigs, props);
    }

    /**
     * 创建分片规则配置（从 Environment 读取）
     */
    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration config = new ShardingRuleConfiguration();

        // 读取所有分片表配置
        String[] tableNames = {"sharding_day", "sharding_month", "sharding_month_accumulate"};
        
        for (String tableName : tableNames) {
            String prefix = "spring.shardingsphere.rules.sharding.tables." + tableName + ".";
            
            String actualDataNodes = env.getProperty(prefix + "actual-data-nodes");
            if (actualDataNodes != null) {
                ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration(
                        tableName,
                        actualDataNodes
                );

                // 分表策略
                String shardingColumn = env.getProperty(prefix + "table-strategy.standard.sharding-column");
                String algorithmName = env.getProperty(prefix + "table-strategy.standard.sharding-algorithm-name");
                
                if (shardingColumn != null && algorithmName != null) {
                    tableRuleConfig.setTableShardingStrategy(
                            new StandardShardingStrategyConfiguration(shardingColumn, algorithmName)
                    );
                }

                config.getTables().add(tableRuleConfig);
            }
        }

        // 绑定表配置（去掉所有空格）
        // 注意：绑定表必须使用相同的分片键和分片策略，否则会报错
        // 由于 sharding_day、sharding_month、sharding_month_accumulate 的分片键和算法不同，
        // 暂时注释掉绑定表配置，避免启动失败
        /*
        String bindingTables = env.getProperty("spring.shardingsphere.rules.sharding.binding-tables");
        if (bindingTables != null && !bindingTables.trim().isEmpty()) {
            String cleanedBindingTables = bindingTables.replaceAll("\\s+", "");
            ShardingTableReferenceRuleConfiguration referenceRuleConfig = 
                    new ShardingTableReferenceRuleConfiguration("binding_ref", cleanedBindingTables);
            config.getBindingTableGroups().add(referenceRuleConfig);
        }
        */

        // 分片算法配置
        String[] algorithmNames = {"shardingDayInline", "shardingMonthInline", "shardingMonthAccumulateInline"};
        
        for (String algoName : algorithmNames) {
            String prefix = "spring.shardingsphere.rules.sharding.sharding-algorithms." + algoName + ".";
            
            String type = env.getProperty(prefix + "type");
            if (type != null) {
                Properties algoProps = new Properties();
                String strategy = env.getProperty(prefix + "props.strategy");
                String className = env.getProperty(prefix + "props.algorithmClassName");
                
                if (strategy != null) algoProps.setProperty("strategy", strategy);
                if (className != null) algoProps.setProperty("algorithmClassName", className);
                
                config.getShardingAlgorithms().put(algoName, new AlgorithmConfiguration(type, algoProps));
            }
        }

        return config;
    }
}