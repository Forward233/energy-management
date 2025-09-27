package com.yunpower.collect.storage.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @title: 动态数据源配置
 * @Author: Jiajiaglam
 * @date: 2023-10-17 10:33
 * @description:
 */
@Configuration
public class DataSourceConfiguration {
    /**
     * 分表数据源名称
     */
    private static final String SHARDING_DATA_SOURCE_NAME = "sharding";

    /**
     * 动态数据源配置项
     */
    @Autowired
    private DynamicDataSourceProperties properties;
    
    @Autowired
    private DefaultDataSourceCreator dataSourceCreator;

    /**
     * shardingjdbc有四种数据源，需要根据业务注入不同的数据源
     * <p>1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
     * <p>2. 主从数据源: masterSlaveDataSource;
     * <p>3. 脱敏数据源：encryptDataSource;
     * <p>4. 影子数据源：shadowDataSource
     */
    @Resource
    private DataSource shardingSphereDataSource;

    /**
     * 将 shardingjdbc 管理的数据源也交给动态数据源管理
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new AbstractDataSourceProvider(dataSourceCreator) {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
                // 只有当 ShardingSphere 数据源存在时才添加
                if (shardingSphereDataSource != null) {
                    dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, shardingSphereDataSource);
                }
                return dataSourceMap;
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
