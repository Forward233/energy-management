package com.yunpower.system.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源配置
 * 
 * @author yunpower
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DataSourceConfiguration {

    /**
     * 手动创建 DefaultDataSourceCreator
     */
    @Bean
    public DefaultDataSourceCreator defaultDataSourceCreator() {
        DefaultDataSourceCreator creator = new DefaultDataSourceCreator();
        // 添加 Druid 数据源创建器
        creator.addCreator(new DruidDataSourceCreator());
        return creator;
    }

    /**
     * 数据源提供者
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(
            DynamicDataSourceProperties properties,
            DefaultDataSourceCreator dataSourceCreator) {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new AbstractDataSourceProvider(dataSourceCreator) {
            @Override
            public Map<String, DataSource> loadDataSources() {
                return createDataSourceMap(datasourceMap);
            }
        };
    }

    /**
     * 将动态数据源设置为首选的
     */
    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProperties properties, 
                                   DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource(
                java.util.Collections.singletonList(dynamicDataSourceProvider));
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }
}
