package com.fota.fotamargin.manager;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author taoyuanming
 * Created on 2018/7/7
 * Description RedisTemplate
 */
@Slf4j
@Component
public class RedisManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;


    /**
     * 添加redis缓存
     * @param key
     * @param value
     */
    public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置过期时间
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireValue(String key, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().getOperations().expire(key, time, timeUnit);
    }

    /**
     * 设置具体日期过期
     * @param key
     * @param date
     */
    public void expireOnDate(String key, Date date) {
        redisTemplate.opsForValue().getOperations().expireAt(key, date);
    }

    /**
     * 添加redis缓存并设置过期时间
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     */
    public void saveValueOnExpire(String key, String value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 添加redis缓存并设置过期时间
     * @param key
     * @param obj
     * @param time
     * @param timeUnit
     */
    public void saveObjectOnExpire(String key, Object obj, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), time, timeUnit);
    }

    /**
     * 删除key
     * @param key
     */
    public void deleteValue(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * Set key to hold the string value if key is absent.
     * @param key
     * @param value
     * @return 不存该key则保存该K-V并返回true，如果已存在该key则无法保存该K-V也不会修改该Key原来的value并返回false
     */
    public Boolean saveValueIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 通过key获取指定Object
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        return JSON.parseObject((String) redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 通过key获取指定泛型的集合
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getArray(String key, Class<T> clazz) {
        return JSON.parseArray((String) redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 同步定时任务
     * @param key 定时任务唯一标示作为redis的key 单独枚举类RedisKeyEnum
     * @param time 取定时任务执行的间隔时间的一半作为key的失效时间
     * @param timeUnit
     * @return 返回true则执行定时任务，返回false则不执行
     */
    public Boolean syncScheduledTask(String key, long time, TimeUnit timeUnit) {
        return setExpireIfAbsent(key, "", time, timeUnit);
    }

    /**
     * setIfAbsent，如果不存在该key即返回true则同时设置该key的过期时间
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    public Boolean setExpireIfAbsent(String key, String value, long time, TimeUnit timeUnit) {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (aBoolean) {
            redisTemplate.opsForValue().getOperations().expire(key, time, timeUnit);
        }
        return aBoolean;
    }

    /**
     * 添加元素到set
     * @param key
     * @param member
     */
    public Long sAdd(String key, String member) {
        return redisTemplate.opsForSet().add(key, member);
    }

    /**
     * 从set移除指定元素
     * @param key
     * @param member
     * @return
     */
    public Boolean sRem(String key, String member) {
        Long l = redisTemplate.opsForSet().remove(key, member);
        if (null != l && l != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("hset({}, {}, {})", key, item, value, e);
            return false;
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            log.error("hget({}, {})", key, item, e);
        }
        return null;
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            log.error("hdel({}, {})", key, item, e);
        }
    }
}
