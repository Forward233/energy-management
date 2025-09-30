package com.yunpower.system.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源配置
 * 确保动态数据源能够正确初始化
 * 
 * @author yunpower
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.dynamic", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataSourceConfig {
    
    /**
     * 动态数据源会由 dynamic-datasource-spring-boot-starter 自动配置
     * 此配置类仅用于确保配置加载顺序正确
     */
}
