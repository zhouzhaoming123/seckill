package com.zzmhome.seckill.service.impl;

import com.zzmhome.seckill.service.ILock;
import com.zzmhome.seckill.utils.UUIDUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public class SimpleRedisLock implements ILock {

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisScript<Boolean> redisScript;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private final static String KEY_PREFIX = "lock:";
    private final static String ID_PREFIX = UUIDUtil.uuid() + "-";

    @Override
    public boolean tryLock(long timeoutSec) {
        //获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        //获取锁
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
        return BooleanUtils.isTrue(success);
    }

    @Override
    public void unLock() {
        //获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        //调用 lua 脚本
        stringRedisTemplate.execute(redisScript, Collections.singletonList(KEY_PREFIX + name), threadId);
    }

//    @Override
//    public void unLock() {
//        //获取线程标识
//        String threadId = ID_PREFIX + Thread.currentThread().getId();
//        //获取锁中标识
//        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
//        //判断标识是否一致
//        if (threadId.equals(id)){
//            //释放锁
//            stringRedisTemplate.delete(KEY_PREFIX + name);
//        }
//    }
}
