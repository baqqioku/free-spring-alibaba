package com.freedom.framework.mysql;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import com.freedom.framework.mysql.type.IDictTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@MapperScan(basePackages = "com.msb.**.mapper")
public class MybatisPlusConfig {

    /**
     * @return
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 数据自动插入拦截器
     * 自动插入的数据包括：创建人、创建时间、修改人、修改时间
     */
   /* @Bean
    public DataOperationInterceptor dataOperationInterceptor() {
        return new DataOperationInterceptor();
    }*/


    /**
     * 注册IDict类型处理器
     * 支持使用枚举充当bean属性，以及Wrapper条件中直接使用枚举
     * 注意会替换默认的枚举处理器，凡是使用枚举当bean属性的在，枚举都需要实现IDict
     */
    @Configuration
    public static class CustomTypeHandlerParser implements ApplicationContextAware {
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
            TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
            typeHandlerRegistry.setDefaultEnumTypeHandler(IDictTypeHandler.class);
        }
    }
}
