package com.zzmhome.seckill.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存工具类
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value),time,unit);
    }

    /**
     * 缓存设置逻辑过期方法
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(redisData));
    }

    /**
     * 缓存穿透解决方法
     * Shop shop = cacheClient.queryWithPassThrough(keyPrefix, id, Shop.class, this::getById, 10L, TimeUnit.MINUTES)
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallBack
     * @param time
     * @param unit
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallBack, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if (StringUtils.isNotEmpty(json)){
            //3.存在，直接返回
            return JSONObject.parseObject(json, type);
        }
        //判断命中的是否空值
        if (json != null){
            //返回一个错误信息
            return null;
        }
        //4.不存在，直接查询数据库
        R r = dbFallBack.apply(id);
        //5.不存在，返回错误
        if (r == null){
            //将空值写入Redis
            stringRedisTemplate.opsForValue().set(key,"",time,unit);
            //返回错误信息
            return null;
        }
        //6.存在，写入Redis
        this.set(key,r,time,unit);
        return r;
    }

    //线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * 逻辑过期解决缓存击穿方法
     * Shop shop = cacheClient.queryWithLogicalExpire(keyPrefix, id, Shop.class, this::getById, 10L, TimeUnit.MINUTES)
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallBack
     * @param time
     * @param unit
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallBack, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if (StringUtils.isEmpty(json)){
            //3.不存在，直接返回
            return JSONObject.parseObject(json, type);
        }
        //4.命中，需要把json反序列化为对象
        RedisData redisData = JSONObject.parseObject(json, RedisData.class);
        R r = JSONObject.parseObject(JSONObject.toJSONString(redisData.getData()),type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //5.判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            //5.1.未过期，直接返回信息
            return r;
        }
        //5.2.已过期，重新缓存重建
        //6.缓存重建
        //6.1.获取互斥锁
        boolean isLock = tryLock(key);
        //6.2.判断是否获取互斥锁
        if (isLock){
            //6.3.成功，开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    //查询数据库
                    R r1 = dbFallBack.apply(id);
                    //写入Redis
                    this.setWithLogicalExpire(key,r1,time,unit);
                }catch (Exception e){
                    throw new RuntimeException();
                }finally {
                    unlock(key);
                }
            });
        }
        //6.4.返回过期的信息
        return r;

    }

    /**
     * redis 加锁
     * @param key
     * @return
     */
    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtils.isTrue(flag);
    }

    /**
     * redis 删除锁
     * @param key
     */
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }

}






















