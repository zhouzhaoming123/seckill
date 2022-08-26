package com.zzmhome.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01(){
        redisTemplate.opsForValue().set("name","zzm");
        String name = String.valueOf(redisTemplate.opsForValue().get("name"));
        System.out.println(name);
    }

    @Test
    public void test02(){
        stringRedisTemplate.opsForValue().set("love", "zyq");
        String love = stringRedisTemplate.opsForValue().get("love");
        System.out.println(love);
    }

}
