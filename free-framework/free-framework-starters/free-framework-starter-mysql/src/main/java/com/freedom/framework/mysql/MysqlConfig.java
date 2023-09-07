package com.freedom.framework.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.freedom")
@MapperScan(basePackages = "com.freedom.**.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
public class MysqlConfig {

    @Bean
    @ConditionalOnBean(DataSource.class)
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager bean = new DataSourceTransactionManager();
        bean.setDataSource(dataSource);
        return bean;
    }

    /*@Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager regTransactionManager () {
        //AtomikosTransactionManager userTransactionManager = new Atomikos
        DataSourceTransactionManager userTransactionManager = new DataSourceTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }*/


}