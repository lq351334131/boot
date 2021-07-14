package com.etocrm.sdk.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.etocrm.sdk.dao", sqlSessionFactoryRef = "db1SqlSessionFactory")
public class DruidConfig {

    @Resource
    private JdbcParamConfig jdbcParamConfig ;

    @Bean("dataSource1")
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(jdbcParamConfig.getUrl());
        datasource.setUsername(jdbcParamConfig.getUserName());
        datasource.setPassword(jdbcParamConfig.getPassword());
        datasource.setDriverClassName(jdbcParamConfig.getDriverClassName());
        datasource.setInitialSize(jdbcParamConfig.getInitialSize());
        datasource.setMinIdle(jdbcParamConfig.getMinIdle());
        datasource.setMaxActive(jdbcParamConfig.getMaxActive());
        datasource.setMaxWait(jdbcParamConfig.getMaxWait());
        return datasource;
    }

    @Bean("db1SqlSessionFactory")
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("dataSource1") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return bean.getObject();
    }
}
