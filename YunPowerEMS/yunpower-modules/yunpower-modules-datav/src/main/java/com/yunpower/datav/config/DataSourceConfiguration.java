package com.yunpower.datav.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @title: 动态数据源配置
 * @Author: Jiajiaglam
 * @date: 2023-10-17 10:33
 * @description: 已由 ShardingSphereConfig 替代，暂时禁用
 */
//@Configuration  // 禁用：使用 ShardingSphereConfig 手动配置
public class DataSourceConfiguration {
    /**
     * 动态数据源配置项
     */
    @Autowired
    private DynamicDataSourceProperties properties;

    /**
     * 数据源创建器
     */
    @Autowired
    private DefaultDataSourceCreator dataSourceCreator;

    /**
     * ShardingSphere 5.3.0+ 使用 ShardingSphereDriver 方式，不再通过 Spring Bean 注入
     * 如果需要分片功能，在 application.yml 或 Nacos 配置：
     * spring.datasource.driver-class-name=org.apache.shardingsphere.driver.ShardingSphereDriver
     * spring.datasource.url=jdbc:shardingsphere:classpath:shardingsphere.yaml
     * 
     * 动态数据源配置直接从 properties 读取
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new AbstractDataSourceProvider(dataSourceCreator) {
            @Override
            public Map<String, DataSource> loadDataSources() {
                return createDataSourceMap(datasourceMap);
            }
        };
    }

    /**
     * 将动态数据源设置为首选的（可选 @Primary）
     * 当spring存在多个数据源时, 自动注入的是首选的对象
     * 设置为主要的数据源之后，就可以支持shardingjdbc原生的配置方式了
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource(java.util.Collections.singletonList(dynamicDataSourceProvider()));
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }
}
