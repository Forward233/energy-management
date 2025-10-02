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
 * @description: 支持动态数据源切换（master、logdb等）
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
     * 动态数据源提供器
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
     * 将动态数据源设置为首选的（@Primary）
     * 当spring存在多个数据源时, 自动注入的是首选的对象
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
