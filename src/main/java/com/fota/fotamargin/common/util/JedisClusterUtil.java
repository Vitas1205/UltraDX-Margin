package com.fota.fotamargin.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;

/**
 * @author taoyuanming
 * Created on 2018/8/2
 * Description Jedis
 */
public class JedisClusterUtil {
    private static final Logger log = LoggerFactory.getLogger(JedisClusterUtil.class);

    private static JedisCluster jedisCluster = BeanUtil.getBean(JedisCluster.class);

    /**
     * 成功获得分布式锁
     */
    private static final String LOCK_SUCCESS = "OK";

    /**
     * 成功释放分布式锁
     */
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * nxxx - NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.
     */
    private static final String SET_IF_NOT_EXIST = "NX";

    /**
     * expx - EX|PX, expire time units: EX = seconds; PX = milliseconds
     */
    private static final String EXPIRE_TIME_UNITS = "PX";

    private static final String EXPIRE_TIME_UNITS_EX = "EX";


    /**
     * 获取Jedis
     * @return
     */
//    private static Jedis getJedis() {
//        return jedisPool.getResource();
//    }

    /**
     * 释放Jedis
     * @param jedis
     */
//    private static void releaseJedis(Jedis jedis) {
//        if (jedis != null) {
//            jedis.close();
//        }
//    }

    /**
     * 如果不存在该key即返回true则同时设置该key的过期时间
     * @param key
     * @param expireTime
     * @return
     */
    public static boolean setIfAbsentEX(String key, Long expireTime) {
        return tryDistributedLockWithTimeUnit(key, "", EXPIRE_TIME_UNITS_EX, expireTime);
    }

    /**
     * 如果不存在该key即返回true则同时设置该key的过期时间
     * @param key
     * @param expireTime
     * @return
     */
    public static boolean setIfAbsentPX(String key, Long expireTime) {
        return tryDistributedLockWithTimeUnit(key, "", EXPIRE_TIME_UNITS, expireTime);
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 过期时间
     * @return 是否获取成功
     */
    public static boolean tryDistributedLock(String lockKey, String requestId, Long expireTime) {
        return tryDistributedLockWithTimeUnit(lockKey, requestId, EXPIRE_TIME_UNITS, expireTime);
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTimeUnits 过期时间单位
     * @param expireTime 过期时间
     * @return 是否获取成功
     */
    public static boolean tryDistributedLockWithTimeUnit(String lockKey, String requestId, String expireTimeUnits, Long expireTime) {
        String result = null;
        try {
            result = jedisCluster.set(lockKey, requestId, SET_IF_NOT_EXIST, expireTimeUnits, expireTime);
        } catch (Exception e) {
            log.error("tryDistributedLock error, lockKey: {}, requestId: {}, expireTime: {}", lockKey, requestId, expireTime);
        }

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = null;
        try {
            result = jedisCluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        } catch (Exception e) {
            log.error("releaseDistributedLock error, lockKey: {}, requestId: {}", lockKey, requestId);
        }

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
