package com.freedom.framework.mysql.config;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Intercepts({@Signature(args = {MappedStatement.class, Object.class}, method = "update", type = Executor.class),
        @Signature(args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query", type = Executor.class)})
public class SqlLogPlugins implements Interceptor {

    private static Logger LOGGER = LoggerFactory.getLogger(SqlLogPlugins.class);


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object paremeter = null;
        if (invocation.getArgs().length > 1) {
            paremeter = invocation.getArgs()[1];
        }
        BoundSql boundSql  = mappedStatement.getBoundSql(paremeter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = showSlq(configuration,boundSql);
        LOGGER.info(sql);
        return invocation.proceed();
    }

    private String showSlq(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if(parameterMappingList.size() >= 0 && parameterObject!=null){
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if(typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())){
                sql =sql.replace("\\?", getParameterValue(parameterObject));
            }else {
                //非原始类型的参数
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappingList) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
