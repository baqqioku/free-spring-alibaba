package com.freedom.framework.redis.lock;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分布式锁配置类
 *
 * @author R
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.lock")
public class LockProperties {
    /**
     * 加锁失败抛出的异常信息
     * 支持spEL，允许的参数有方法的入参以及 #waitTime
     */
    String errorMsg = "系统繁忙，请稍后再试。";
    /**
     * 锁类型
     */
    private LockType type = LockType.REDIS_REENTRANT_LOCK;
    /**
     * 锁key的前缀
     */
    private String prefix = "@lock";


    public enum LockType {
        /**
         * Redis可重入锁（默认）
         */
        REDIS_REENTRANT_LOCK,
        /**
         * Redis公平锁
         */
        REDIS_FAIR_LOCK,
        /**
         * Redis自旋锁
         */
        REDIS_SPIN_LOCK,

    }

}
