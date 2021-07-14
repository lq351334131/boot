package com.etocrm.sdk.config;

//@Configuration
//@MapperScan(basePackages = "com.etocrm.sdk.db4dao", sqlSessionFactoryRef = "db4SqlSessionFactory")
public class Db4Config {

//    @Bean("db4DataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.db4")
//    public DataSource getDb4DataSource(){
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean("db4SqlSessionFactory")
//    public SqlSessionFactory db4SqlSessionFactory(@Qualifier("db4DataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:db4mapper/*.xml"));
//        return bean.getObject();
//    }
//
//    @Bean("db4SqlSessionTemplate")
//    public SqlSessionTemplate db4SqlSessionTemplate(@Qualifier("db4SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
}
