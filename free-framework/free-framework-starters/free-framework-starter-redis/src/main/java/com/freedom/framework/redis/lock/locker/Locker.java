package com.freedom.framework.redis.lock.locker;

import java.util.concurrent.TimeUnit;

public interface Locker {

    /**
     * 加锁
     *
     * @param lockKey   锁key
     * @param leaseTime 自动释放时间
     * @param waitTime  加锁等待时间
     * @param unit      时间单位
     * @return 是否加锁成功
     * @throws InterruptedException InterruptedException
     */
    boolean tryLock(String lockKey, int leaseTime, int waitTime, TimeUnit unit) throws InterruptedException;

    void lock(String lockKey, int leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 解锁
     */
    void unlock();
}
