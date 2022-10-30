package com.zzmhome.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<Boolean> redisScript;

    @Test
    void testLock01() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //占位，如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        //如果占位成功，进行正常操作
        if (isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            Integer.parseInt("xxxxxxx");
            //操作结束，请删除
            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }

    @Test
    void testLock02() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //占位，如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
        //如果占位成功，进行正常操作
        if (isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            Integer.parseInt("xxxxxxx");
            //操作结束，请删除
            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }

    @Test
    void testLock03() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //占位，如果key不存在才可以设置成功
        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", value,100, TimeUnit.SECONDS);
        //如果占位成功，进行正常操作
        if (isLock){
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            System.out.println(valueOperations.get("k1"));
            //操作结束，请删除
            Boolean result = redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(result);
        }else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }

}
