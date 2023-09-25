package com.freedom.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import java.util.Map;

/**
 * Spring上下文工具
 *
 * @author luzhan
 * @date 2022年3月18日
 */
@Component
public class SpringContextUtil implements ApplicationContextAware, EmbeddedValueResolverAware {

    private static ApplicationContext context;
    private static StringValueResolver stringValueResolver;

    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取实例，如果找不到会报错
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> Map<String, T> getBeanMap(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    /**
     * 获取当前配置属性
     */
    public static String getProperty(String configKey) {
        return context.getEnvironment().getProperty(configKey);
    }

    /**
     * 获取spring value，和@Value的效果相同
     * 注意：如果是获取配置，找不到配置会报异常
     *
     * @param spEL spring表达式，须用"${spEL}"括起来，否则返回值和入参相同
     * @return 表达式的值
     */
    public static String getSpringValue(String spEL) {
        return stringValueResolver.resolveStringValue(spEL);
    }

    /**
     * 获取当前profile
     */
    public static String getProfile() {
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        return activeProfiles.length == 0 ? "dev" : activeProfiles[0];
    }

    /**
     * 当前环境是否是开发环境
     */
    public static boolean isDev() {
        return "dev".equals(getProfile());
    }

    /**
     * 当前环境是否是测试环境
     */
    public static boolean isTest() {
        return "test".equals(getProfile());
    }

    /**
     * 当前环境是否是生产环境
     */
    public static boolean isProd() {
        return "prod".equals(getProfile());
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.context = applicationContext;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        SpringContextUtil.stringValueResolver = resolver;
    }
}
