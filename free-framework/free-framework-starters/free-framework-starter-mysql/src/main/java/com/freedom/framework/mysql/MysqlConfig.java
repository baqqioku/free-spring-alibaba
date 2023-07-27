package com.freedom.framework.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.freedom")
@MapperScan(basePackages = "com.freedom.**.mapper")
public class MysqlConfig {

}