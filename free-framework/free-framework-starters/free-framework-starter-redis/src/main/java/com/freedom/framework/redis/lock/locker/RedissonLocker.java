package com.freedom.framework.redis.lock.locker;

import com.freedom.framework.redis.lock.LockProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class RedissonLocker implements Locker {
    private static final ThreadLocal<RLock> LOCK_THREAD_LOCAL = new ThreadLocal<>();
    private final LockProperties.LockType lockType;
    private final RedissonClient redissonClient;



    @Override
    @SuppressWarnings("all")
    public boolean tryLock(String lockKey, int leaseTime, int waitTime, TimeUnit unit) throws InterruptedException {
        RLock lock = getLock(lockKey);
        LOCK_THREAD_LOCAL.set(lock);
        log.info("加锁，thread：{}，key：{}", Thread.currentThread().getId(), lockKey);
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    public boolean lock(String lockKey, int leaseTime, int waitTime, TimeUnit unit) throws InterruptedException {
        RLock lock = getLock(lockKey);
        LOCK_THREAD_LOCAL.set(lock);
        log.info("加锁，thread：{}，key：{}", Thread.currentThread().getId(), lockKey);
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public void unlock() {
        log.info("解锁，thread：{}，key：{}", Thread.currentThread().getId(), LOCK_THREAD_LOCAL.get().getName());
        RLock lock = LOCK_THREAD_LOCAL.get();
        if (lock!=null && lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
        LOCK_THREAD_LOCAL.remove();
    }

    private RLock getLock(String lockKey) {
        switch (lockType) {
            case REDIS_FAIR_LOCK:
                return redissonClient.getFairLock(lockKey);
            case REDIS_SPIN_LOCK:
                return redissonClient.getSpinLock(lockKey);
            case REDIS_REENTRANT_LOCK:
                return redissonClient.getLock(lockKey);
            default:
                throw new IllegalArgumentException("错误的锁类型");
        }
    }
}
