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
 * @Author: Jiajiaglam
 * @date: 2023-10-17 10:33
 * @description: 同时支持动态数据源（master、logdb）和分片数据源（ds0/sharding）
 */
@Configuration
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
     * ShardingSphere 分片数据源（由 ShardingSphereConfig 创建）
     */
    @Autowired(required = false)
    private DataSource shardingSphereDataSource;

    /**
     * 动态数据源提供器：整合普通数据源和 ShardingSphere 数据源
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new AbstractDataSourceProvider(dataSourceCreator) {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
                // 如果存在 ShardingSphere 数据源，添加到动态数据源 Map 中
                if (shardingSphereDataSource != null) {
                    dataSourceMap.put("sharding", shardingSphereDataSource);
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
