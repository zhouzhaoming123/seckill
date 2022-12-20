package com.zzmhome.seckill;

import com.zzmhome.seckill.pojo.SeckillOrder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@SpringBootTest
@Slf4j
public class SeckillTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        //lock.lua脚本位置
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private LinkedBlockingQueue<SeckillOrder> orderTasks= new LinkedBlockingQueue<>(1024 * 1024);

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(new SeckillOrderHandler());
    }

    private class SeckillOrderHandler implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    //1.获取队列中的订单信息
                    SeckillOrder seckillOrder = orderTasks.take();
                    //TODO 2.创建订单

                } catch (Exception e) {
                    log.error("处理订单异常",e);
                }
            }
        }
    }


    @Test
    void testInsertInfo(){
        stringRedisTemplate.opsForValue().set("seckill:stock:1","10");
    }

    @Test
    void testSeckillLua(){
        Long result = stringRedisTemplate.execute(SECKILL_SCRIPT, Collections.emptyList(), "1", "11");
        val value = result.intValue();
        if (value != 0){
            System.out.println(value==1?"库存不足":"重复下单");
            return;
        }
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(1L);
        seckillOrder.setUserId(11L);
        orderTasks.add(seckillOrder);
        System.out.println("下单成功");
    }

}
