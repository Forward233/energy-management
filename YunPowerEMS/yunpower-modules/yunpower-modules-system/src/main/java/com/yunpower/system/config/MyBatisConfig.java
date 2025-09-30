package com.yunpower.system.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis配置类
 * 由于排除了DataSourceAutoConfiguration，需要手动配置SqlSessionFactory
 */
@Configuration
public class MyBatisConfig {

    /**
     * SqlSessionFactory配置
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        
        // 设置MyBatis配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        
        // 设置mapper文件位置
        sqlSessionFactory.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml")
        );
        
        // 设置类型别名包
        sqlSessionFactory.setTypeAliasesPackage("com.yunpower.system.domain,com.yunpower.system.api.domain");
        
        // 添加分页插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        sqlSessionFactory.setPlugins(interceptor);
        
        return sqlSessionFactory.getObject();
    }

    /**
     * SqlSessionTemplate配置
     */
    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * MyBatis-Plus配置定制
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 其他自定义配置可以在这里添加
            configuration.setDefaultExecutorType(org.apache.ibatis.session.ExecutorType.SIMPLE);
        };
    }
}
