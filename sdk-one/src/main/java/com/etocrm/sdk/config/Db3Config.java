package com.etocrm.sdk.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.etocrm.sdk.db3dao", sqlSessionFactoryRef = "db3SqlSessionFactory")
public class Db3Config {

    @Bean("db3DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db3")
    public DataSource getDb3DataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("db3SqlSessionFactory")
    public SqlSessionFactory db3SqlSessionFactory(@Qualifier("db3DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:db3mapper/*.xml"));
        return bean.getObject();
    }

    @Bean("db3SqlSessionTemplate")
    public SqlSessionTemplate db3SqlSessionTemplate(@Qualifier("db3SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
