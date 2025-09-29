package com.yunpower.datav.config;

import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * ShardingSphere DataSource 配置
 * 从配置文件读取数据源配置并创建ShardingSphere数据源
 */
@Configuration
public class ShardingSphereDataSourceConfig {

    @Value("${spring.shardingsphere.datasource.ds0.jdbc-url:}")
    private String jdbcUrl;

    @Value("${spring.shardingsphere.datasource.ds0.username:}")
    private String username;

    @Value("${spring.shardingsphere.datasource.ds0.password:}")
    private String password;

    @Value("${spring.shardingsphere.datasource.ds0.driver-class-name:}")
    private String driverClassName;

    /**
     * 创建ShardingSphere数据源
     * 只有当配置了jdbc-url时才创建
     */
    @Bean
    @ConditionalOnMissingBean(ShardingSphereDataSource.class)
    @ConditionalOnProperty(name = "spring.shardingsphere.datasource.ds0.jdbc-url")
    public DataSource shardingSphereDataSource() throws SQLException {
        // 创建基础数据源
        HikariDataSource ds0 = new HikariDataSource();
        ds0.setJdbcUrl(jdbcUrl);
        ds0.setUsername(username);
        ds0.setPassword(password);
        ds0.setDriverClassName(driverClassName);

        // 数据源映射
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0", ds0);

        // 规则配置（空的，不配置分片规则）
        Collection<RuleConfiguration> ruleConfigs = new ArrayList<>();

        // ShardingSphere属性配置
        Properties props = new Properties();
        props.setProperty("sql-show", "true");

        // 使用工厂方法创建ShardingSphere数据源
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfigs, props);
    }
}
