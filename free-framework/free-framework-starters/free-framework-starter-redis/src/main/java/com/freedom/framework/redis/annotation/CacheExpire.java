package com.freedom.framework.redis.annotation;

import com.freedom.framework.redis.consts.Time;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheExpire {

    /**
     * 失效时间（秒）,负数或0表示永不过期
     * 传参请使用Time类中的静态常量，使语义性更好
     * 示例：@CacheExpire(30 * SECOND)
     *
     * @see Time 常用时间单位
     */
    int value();

    /**
     * 上下浮动范围（秒）
     * 根据浮动范围生成随机秒数加在缓存失效时间上，防止缓存穿透
     * 默认10毫秒
     */
    double floatRange() default 0.01;
}
