package com.freedom.framework.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.*;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RedisClient {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 指定缓存失效时间
     * (命令EXPIRE)
     *
     * @param key      键
     * @param time     时间（秒）
     * @param timeUnit 时间单位
     * @return true / false
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        if (time > 0) {
            return redisTemplate.expire(key, time, timeUnit);
        }
        return false;
    }

    /**
     * 根据 key 获取过期时间
     * (命令TTL)
     *
     * @param key 键
     * @return 秒
     */
    public long ttl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     * (命令EXISTS)
     *
     * @param key 键
     * @return true / false
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存(不存在key不会抛异常)
     * (命令 DEL)
     *
     * @param keys 集合
     */
    public long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 删除缓存
     * (命令DEL)
     *
     * @param key 键（一个或者多个）
     */
    public boolean delete(String... key) {

        if (key != null && key.length > 0) {
            if (key.length == 1) {
                return redisTemplate.delete(key[0]);
            } else {
                return redisTemplate.delete(Arrays.asList(key)) > 0;
            }
        }
        return true;
    }

    /**
     * 按表达式删除缓存，使用scan命令实现，不会有太多性能影响
     *
     * @param pattern 表达式
     */
    public long deleteAll(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("参数不正确");
        }
        return redisTemplate.delete(scan(pattern));
    }

    /**
     * 查询所有满足表达式的key
     * （命令KEYS，实际是利用SCAN命令实现，不会产生阻塞）
     *
     * @param pattern 表达式
     * @return 符合表达式的key的集合
     * @complexity O(n)
     */
    @SuppressWarnings("all")
    @NonNull
    public Set<String> scan(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("参数不正确");
        }
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
                    Set<String> keys = new HashSet<>();
                    Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(1000).build());
                    while (cursor.hasNext()) {
                        keys.add(new String(cursor.next()));
                    }
                    return keys;
                }
        );
    }


//    ============================== String ==============================

    /**
     * 放入缓存
     * (命令 SET)
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 放入缓存
     * (命令 SET)
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存（string数据结构） 通过泛型T指定缓存数据类型
     * (命令 GET)
     *
     * @param key 键
     * @return 值
     * @see #hGetAll
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存（string数据结构） 通过泛型T指定缓存数据类型
     * (命令 GETSET)
     *
     * @param key 键
     * @return 值
     * @see #hGetAll
     */
    @SuppressWarnings("unchecked")
    public <T> T getSet(String key, T newValue) {
        return key == null ? null : (T) redisTemplate.opsForValue().getAndSet(key, newValue);
    }


    /**
     * 缓存普通键值对，并设置失效时间
     *
     * @param key     键
     * @param value   值
     * @param seconds 时间（秒），如果 time <= 0 则不设置失效时间
     */
    public void set(String key, Object value, int seconds) {
        if (seconds > 0) {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, seconds);
        }
    }

    /**
     * 当key不存在时放入键值对
     * 如果已经存在key返回false
     *
     * @param key   键
     * @param value 值
     * @return true/false
     */
    public boolean setNx(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }


    /**
     * 当key不存在时放入键值对，并在指定时间后自动删除
     * 如果已经存在key返回false
     *
     * @param key      键
     * @param value    值
     * @param time     时间
     * @param timeUnit 时间单位
     * @return true/false
     */
    public boolean setNx(String key, Object value, long time, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
    }

    /**
     * 递增
     * 如果不存在key将自动创建
     * (命令INCR)
     *
     * @param key       键
     * @param increment 递增大小
     * @return 递增后的值
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> T increment(String key, T increment) {
        return (T) redisTemplate.opsForValue().increment(key, increment.doubleValue());
    }

    /**
     * 递增1
     * 如果不存在key将自动创建
     * (命令INCR)
     *
     * @param key 键
     * @return 递增后的值
     */
    public long increment(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    /**
     * 递减
     * 如果不存在key将自动创建
     * (命令DECR)
     *
     * @param key       键
     * @param decrement 递减大小
     * @return 递减后的值
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> T decrement(String key, T decrement) {
        return (T) redisTemplate.opsForValue().increment(key, -decrement.longValue());
    }

    /**
     * 递减1
     * 如果不存在key将自动创建
     * (命令DECR)
     *
     * @param key 键
     * @return 递减后的值
     */
    public long decrement(String key) {
        return decrement(key, 1L);
    }

//    ============================== Map ==============================

    /**
     * 往指定HashMap中添加一对键值对，key不存在将自动创建
     * (命令HSET)
     *
     * @param name  HashMap的名字
     * @param key   添加的键
     * @param value 添加的值
     */
    public void hSet(String name, String key, Object value) {
        redisTemplate.opsForHash().put(name, key, value);
    }

    /**
     * 往指定HashMap中添加另一个map
     * (命令HSET)
     *
     * @param name HashMap的名字
     * @param map  值
     */
    public void hSet(String name, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(name, map);
    }

    /**
     * 从指定HashMap中获取指定key的值
     * （命令HGET）
     *
     * @param mapName HashMap的名字（no null）
     * @param key     HashMap中的键（no null）
     * @param <T>     根据实际类型自定义
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T hGet(String mapName, String key) {
        return (T) redisTemplate.opsForHash().get(mapName, key);
    }

    /**
     * 从指定HashMap中获取多个key的值
     * （命令HMGET）
     *
     * @param mapName HashMap的名字（no null）
     * @param key     HashMap中的键，传多个（no null）
     * @param <T>     根据实际类型自定义
     * @return list
     */
    public <T> List<T> hMultiGet(String mapName, String... key) {
        return redisTemplate.<String, T>opsForHash().multiGet(mapName, Arrays.asList(key));
    }

    /**
     * 获取指定的HashMap
     * （命令HGETALL）
     *
     * @param mapName HashMap的名字（no null）
     * @return HashMap
     */
    public <T> Map<String, T> hGetAll(String mapName) {
        return redisTemplate.<String, T>opsForHash().entries(mapName);
    }


    /**
     * 删除 HashMap 中的值
     * (命令HDEL)
     *
     * @param mapName HashMap的名字
     * @param keys    HashMap中的key（可以多个，no null）
     * @return 成功删除个数
     */
    public long hDelete(String mapName, Object... keys) {
        return redisTemplate.opsForHash().delete(mapName, keys);
    }

    /**
     * map 递增1
     * (命令 HINCRBY)
     *
     * @param hashMapName HashMap的名字（no null）
     * @param key         HashMap中的key（no null）
     * @return true / false
     */
    public long hIncrement(String hashMapName, String key) {
        return redisTemplate.opsForHash().increment(hashMapName, key, 1);
    }

    /**
     * map 递增指定值
     * (命令 HINCRBY)
     *
     * @param hashMapName HashMap的名字（no null）
     * @param key         HashMap中的key（no null）
     * @return true / false
     */
    public Long hIncrementBy(String hashMapName, String key, long delta) {
        return redisTemplate.opsForHash().increment(hashMapName, key, delta);
    }

    /**
     * 是否存在hashkey
     * (命令 HEXISTS)
     *
     * @param hashMapName HashMap的名字（no null）
     * @param hashKey     HashMap中的key（no null）
     * @return true / false
     */
    public boolean hExists(String hashMapName, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(hashMapName, hashKey);
    }

    /**
     * 设置map中的值，只有当key不存在时，才能设置成功
     * (命令 HSETNX)
     *
     * @param hashMapName HashMap的名字
     * @param hashKey     key（no null）
     * @param value       value（no null）
     * @return true / false
     */
    public boolean hSetNx(String hashMapName, String hashKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(hashMapName, hashKey, value);
    }

    /**
     * 获取map的大小
     * (命令 HLEN)
     *
     * @param hashMapName HashMap的名字
     * @return map size
     */
    public Long hSize(String hashMapName) {
        return redisTemplate.opsForHash().size(hashMapName);
    }

    /**
     * 获取map中的所有key
     * (命令 HKEYS)
     *
     * @param hashMapName HashMap的名字
     * @return all keys
     */
    public Set<String> hKeys(String hashMapName) {
        return redisTemplate.<String, Object>opsForHash().keys(hashMapName);
    }

    /**
     * 获取map中的所有value
     * (命令 HVALS)
     *
     * @param hashMapName HashMap的名字
     * @return all values
     */
    public <T> List<T> hValues(String hashMapName) {
        return redisTemplate.<String, T>opsForHash().values(hashMapName);
    }


    //    ============================== zset ==============================

    /**
     * 添加zset元素 有则覆盖
     *
     * @param key
     * @param v
     * @param score
     * @return
     */
    public <T> boolean zAdd(String key, T v, long score) {
        return redisTemplate.opsForZSet().add(key, v, score);
    }

    /**
     * 批量添加zset元素
     * (命令 ZADD)
     *
     * @param key
     * @param tuples
     * @return
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * 移除指定位置的zset元素
     * (命令 ZREVRANGE)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 添加zset元素 有则覆盖
     * (命令 ZREM)
     *
     * @param key
     * @return
     */
    public Long zRemove(String key, Object... vs) {
        return redisTemplate.opsForZSet().remove(key, vs);
    }

    /**
     * zset 元素个数
     * (命令 ZCARD)
     *
     * @param key
     * @return
     */
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 如果不存在则添加zset元素
     *
     * @param key
     * @param v
     * @param score
     * @return
     */
    public <T> boolean zAddNx(String key, T v, long score) {
        return redisTemplate.opsForZSet().add(key, v, score);
    }

    /**
     * zset中是否存在对应元素
     *
     * @param key
     * @param v
     * @return
     */
    public <T> boolean zExists(String key, T v) {
        return redisTemplate.opsForZSet().rank(key, v) != null;
    }

    /**
     * 自增zset score
     * (命令 ZINCRBY)
     *
     * @param key
     * @param v
     * @param delta
     * @return
     */
    public <T> Double zIncrementBy(String key, T v, Double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, v, delta);
    }

    /**
     * 获取指定key的所有元素
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> LinkedHashSet<T> zAll(String key) {
        return (LinkedHashSet<T>) redisTemplate.opsForZSet().range(key, 0, -1);
    }

    /**
     * 获取指定位置元素
     *
     * @param key
     * @param index
     * @param <T>
     * @return
     */
    public <T> T zGet(String key, int index) {
        Set<Object> objects = redisTemplate.opsForZSet().range(key, index, index + 1);
        return (T) objects.iterator().next();
    }

    /**
     * zset返回指定元素的索引
     * (命令 ZRANK)
     *
     * @param key
     * @param v
     * @return
     */
    public Long zRank(String key, Object v) {
        return redisTemplate.opsForZSet().rank(key, v);
    }

    /**
     * zset通过索引区间获取指定区间内的元素
     * (命令 ZRANGE)
     *
     * @param key
     * @param start 开始索引
     * @param end   结束索引
     * @return 元素集合
     */
    public <T> Set<T> zRange(String key, long start, long end) {
        return (Set<T>) redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * zset查询元素分数值
     * (命令 ZSCORE)
     *
     * @param key
     * @param v
     * @return 元素分数值
     */
    public Double zScore(String key, Object v) {
        return redisTemplate.opsForZSet().score(key, v);
    }

    /**
     * zset查询指定元素排名
     * (命令 ZREVRANK)
     *
     * @param key
     * @param v
     * @return 排名
     */
    public Long zReverseRank(String key, Object v) {
        return redisTemplate.opsForZSet().reverseRank(key, v);
    }

    /**
     * 通过分数返回zset指定区间内的元素
     * (命令 ZRANGEBYSCORE)
     *
     * @param key
     * @param min 最小分数值
     * @param max 最大分数值
     * @return 元素集合
     */
    public <T> Set<T> zRangeByScore(String key, double min, double max) {
        return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 移除zset中指定分数区间的元素
     * (命令 ZREMRANGEBYRANK)
     *
     * @param key
     * @param min 最小分数值
     * @param max 最大分数值
     * @return 删除元素个数
     */
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 获取zset中指定分数区间的元素数量
     * (命令 ZCOUNT)
     *
     * @param key
     * @param min 最小分数值
     * @param max 最大分数值
     * @return 删除元素个数
     */
    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取指定范围分数的元素
     * (命令 ZREVRANGEBYSCORE )
     *
     * @param key
     * @param min    最小分数值
     * @param max    最大分数值
     * @param offset 偏移量
     * @param count  最大元素集合
     * @return 元素集合
     */
    public <T> Set<T> zReverseRangeByScore(String key, double min, double max, long offset, long count) {
        return (Set<T>) redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * zset根据得分范围获取元素（倒序）
     * 命令：ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
     * 时间复杂度：O(log(N)+M) N是zest长度，M是返回数量
     *
     * @param key    redis key
     * @param min    最小分数（含）
     * @param max    最大分数（含）
     * @param offset 偏移量
     * @param count  返回数量
     * @param <T>
     * @return 包含元素和分数的TypedTuple集合
     * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    public <T> Set<ZSetOperations.TypedTuple<T>> zReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return ((RedisTemplate<String, T>) redisTemplate).opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    //    ============================== set ==============================

    /**
     * 新增set 元素
     *
     * @param key
     * @param v
     * @return
     */
    public <T> Long sAdd(String key, T v) {
        return redisTemplate.opsForSet().add(key, v);
    }

    /**
     * 获取set所有元素
     * (命令 SMEMBERS )
     *
     * @param key
     * @return
     */
    public <T> Set<T> sMembers(String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 新增set元素
     *
     * @param key
     * @param values
     */
    public <T> void sAdd(String key, T... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 批量移除set元素
     *
     * @param key
     * @param values
     * @return 删除元素数量
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取set的元素数量
     * (命令 SCARD)
     *
     * @param key
     * @return 元素数量
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * set判断元素是否存在
     * (命令 SISMEMBER)
     *
     * @param key
     * @return true / false
     */
    public boolean sExists(String key, Object v) {
        return redisTemplate.opsForSet().isMember(key, v);
    }

    /**
     * set判断元素是否存在
     * (命令 SISMEMBER)
     *
     * @param key
     * @return true / false
     */
    public boolean sIsMember(String key, Object v) {
        return redisTemplate.opsForSet().isMember(key, v);
    }

    /**
     * 移除并返回set中的一个随机元素
     * (命令 SPOP)
     *
     * @param key
     * @return 元素
     */
    public <T> T sPop(String key) {
        return (T) redisTemplate.opsForSet().pop(key);
    }

    /**
     * 随机返回set中的一个或者多个元素(不移除)
     * (命令 SRANDMEMBER)
     *
     * @param key
     * @param count 元素数量
     * @return 元素集合
     */
    public <T> List<T> sRandomMembers(String key, long count) {
        return (List<T>) redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 获取第一个set和其他set的差集
     * (命令 SDIFF)
     *
     * @param key
     * @param otherKey
     * @return 元素集合
     */
    public <T> Set<T> sDifference(String key, String otherKey) {
        return (Set<T>) redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 获取第一个set和其他set的差集
     * (命令 SDIFF)
     *
     * @param key
     * @param otherKeys
     * @return 元素集合
     */
    public <T> Set<T> sDifference(String key, Collection<String> otherKeys) {
        return (Set<T>) redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 获取第一个set和其他set的并集
     * (命令 SUNION)
     *
     * @param key
     * @param otherKey
     * @return 元素集合
     */
    public <T> Set<T> sUnion(String key, String otherKey) {
        return (Set<T>) redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 获取第一个set和其他set的并集
     * (命令 SUNION)
     *
     * @param key
     * @param otherKeys
     * @return 元素集合
     */
    public <T> Set<T> sUnion(String key, Collection<String> otherKeys) {
        return (Set<T>) redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 获取第一个set和其他set的交集
     * (命令 SINTER)
     *
     * @param key
     * @param otherKey
     * @return 元素集合
     */
    public <T> Set<T> sIntersect(String key, String otherKey) {
        return (Set<T>) redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 获取第一个set和其他set的交集
     * (命令 SINTER)
     *
     * @param key
     * @param otherKeys
     * @return 元素集合
     */
    public <T> Set<T> sIntersect(String key, Collection<String> otherKeys) {
        return (Set<T>) redisTemplate.opsForSet().intersect(key, otherKeys);
    }


    //    ============================== list ==============================

    /**
     * 查询list中指定位置元素
     * (命令 LINDEX)
     *
     * @param key   key
     * @param index 位置
     */
    public <T> T lIndex(String key, long index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 从左边塞入元素
     *
     * @param key
     * @param v
     */
    public <T> Long lPush(String key, T v) {
        return redisTemplate.opsForList().leftPush(key, v);
    }

    /**
     * 从左边塞入元素
     *
     * @param key
     * @param values
     */
    public Long lPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 从右边取出元素
     * (命令 LRANGE)
     *
     * @param key
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    public <T> List<T> lRange(String key, long start, long end) {
        return (List<T>) redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 做左边拉元素
     * 命令 (LPOP)
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T lPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }


    /**
     * 从右边批量塞入元素
     *
     * @param key
     * @param vs
     */
    public <T> Long rPushAll(String key, T... vs) {
        return redisTemplate.opsForList().rightPushAll(key, vs);
    }

    /**
     * 从右边批量塞入元素
     *
     * @param key
     * @param vs
     */
    public <T> Long rPush(String key, T vs) {
        return redisTemplate.opsForList().rightPush(key, vs);
    }

    /**
     * 删除右边的元素
     *
     * @param key
     * @param removeCount
     */
    public void rRemove(String key, Long removeCount) {
        redisTemplate.opsForList().trim(key, 0, lSize(key) - removeCount);
    }

    /**
     * list集合中元素个数
     * 命令 (LLEN)
     *
     * @param key
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * list集合中元素个数
     * 命令 (LLEN)
     *
     * @param key
     */
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 移除list集合中元素
     * 命令 (LMOVE)
     *
     * @param k
     * @param v
     */
    public Long lRemove(String k, Object v) {
        return redisTemplate.opsForList().remove(k, 0, v);
    }

    /**
     * 批量查询
     *
     * @param key
     */
    public <T> List<T> lAll(String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * list截取列表
     * (命令 LTRIM)
     *
     * @param key   key
     * @param start
     * @param end
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * list设置指定位置的元素
     * (命令 LSET)
     *
     * @param key   key
     * @param index 位置
     * @param v     value
     */
    public void lSet(String key, long index, Object v) {
        redisTemplate.opsForList().set(key, index, v);
    }

    //    ============================== Lock ==============================

    /**
     * 获取Redis分布式锁
     *
     * @param key 锁的key
     * @return RedisLock
     */
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    //    ============================== pipelined ==============================

    /**
     * 管道操作
     * 一次性提交多个命令，提升效率，适用于批量操作
     *
     * @param consumer
     * @param <T>
     * @return 命令结果
     */
    public <T> List<T> pipelined(Consumer<RedisOperations<String, Object>> consumer) {
        return (List<T>) redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                consumer.accept(operations);
                return null;
            }
        });
    }

    //    ============================== hyperLogLogAdd ==============================
    /**
     * HyperLogLog是用来做基数统计的算法，它提供不精确的去重计数方案（这个不精确并不是非常不精确），标准误差是0.81%，
     * 对于UV这种统计来说这样的误差范围是被允许的。HyperLogLog的优点在于，输入元素的数量或者体积非常大时，
     * 基数计算的存储空间是固定的。在Redis中，每个HyperLogLog键只需要花费12KB内存，就可以计算接近2^64个不同的基数。
     * ————————————————
     * HyperLogLog指令都是pf(PF)开头，这是因为HyperLogLog的发明人是Philippe Flajolet，pf是他的名字的首字母缩写。
     */

    /**
     * hyperLogLogAdd 加入元素
     * (命令 PFADD)
     *
     * @param key   key
     * @param value value
     */
    public void pfAdd(String key, Object value) {
        redisTemplate.opsForHyperLogLog().add(key, value);
    }

    /**
     * hyperLogLogAdd 获取元素数量
     * (命令 PFCOUNT)
     *
     * @param key key
     */
    public Long pfCount(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    //    ============================== bitmap ==============================

    /**
     * bitmap设置指定位置 为 1或0
     * (命令 SETBIT)
     *
     * @param key    key
     * @param offset 偏移量
     * @param value      true / false
     */
    public boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * bitmap获取指定位置结果
     * (命令 GETBIT)
     *
     * @param key    key
     * @param offset 偏移量
     * @return true / false
     */
    public boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * bitmap 统计和，所有位置为1的总数量
     * (命令 BITCOUNT)
     *
     * @param key key
     */
    public long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
    }

    /**
     * bitField
     * (命令 BITFIELD)
     *
     * @param key                 key
     * @param bitFieldSubCommands demo: BitFieldSubCommands bitFieldSubCommands =BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.UINT_16).valueAt(0);
     */
    public List<Long> bitField(String key, BitFieldSubCommands bitFieldSubCommands) {
        return redisTemplate.opsForValue().bitField(key, bitFieldSubCommands);
    }


}
