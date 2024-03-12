package com.freedom.framework.redis.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 指定锁的key，使用spEL表达式
     * <p>
     * 示例：<br>
     * 1.使用方法入参作为key： key="#productId"<br>
     * 2.方法入参是对象时，以对象中的属性作为key： key="#product.id"<br>
     * 3.多个入参联合作为key： key={"#productId", "#userId"}<br>
     */
    @AliasFor("value")
    String[] key() default {};

    /**
     * 指定组，同一组、同一key的锁，同一时间只有一个线程能进入
     * <p>
     * 适合用在多个方法需要加一把锁的情况<br>
     * 如果不指定group，默认group为方法坐标（@类名.方法名），此时@Lock等于synchronized（分布式的），即同一时间只有一个线程能进入该方法
     * 生成redis key的格式为：@lock:[group]:([key1],[key2]...)
     */
    String group() default "";

    /**
     * 加锁后自动释放时间
     * 默认自动续期（-1）
     */
    int leaseTime() default -1;

    /**
     * 最大等待时长，超过后还未加锁成功则抛出异常
     * 默认单位：秒，设置0表示不等待，建议配合自定义异常信息使用
     */
    int waitTime() default 2;

    /**
     * 超时后抛出异常的错误信息
     * 支持spEL，允许的参数有方法的入参以及等待时间"#waitTime"
     */
    String errorMsg() default "${spring.lock.errorMsg}";

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    @AliasFor("key")
    String[] value() default {};

}
