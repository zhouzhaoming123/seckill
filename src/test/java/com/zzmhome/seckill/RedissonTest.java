package com.zzmhome.seckill;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@SpringBootTest
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedissonClient redissonClient2;

    @Autowired
    private RedissonClient redissonClient3;

    private RLock lock;

    @BeforeEach
    void setUp() {
        RLock lock1 = redissonClient.getLock("lock:test:" + 1);
        RLock lock2 = redissonClient2.getLock("lock:test:" + 1);
        RLock lock3 = redissonClient3.getLock("lock:test:" + 1);

        // 创建连锁 MultiLock   
        lock = redissonClient.getMultiLock(lock1,lock2,lock3);
    }

    @Test
    public void testRedisson() throws InterruptedException{
        //获取锁（可重入），指定锁名称
        //RLock lock = redissonClient.getLock("lock:test:" + 1);
        //尝试获取锁
        boolean isLock = lock.tryLock(1L, TimeUnit.SECONDS);

        //获取锁（可重入），指定锁名称
        RLock lock2 = redissonClient.getLock("lock:test:" + 1);
        //尝试获取锁
        boolean isLock2 = lock2.tryLock();
        if (isLock2){
            try {
                System.out.println("执行业务2");
            } finally {
                lock2.unlock();
            }
        }

        if (isLock){
            try {
                System.out.println("执行业务1");
            } finally {
                lock.unlock();
            }
        }
    }

}
