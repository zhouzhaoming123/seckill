package com.zzmhome.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzmhome.seckill.pojo.Goods;
import com.zzmhome.seckill.service.GoodsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化缓存
 * Data: 2023/1/4
 * Author: zhouzm
 * ---------------------------
 */
@Component
public class RedisHandler implements InitializingBean {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsService goodsService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化缓存
        // 1.查询商品信息
        List<Goods> goodsList = goodsService.list();
        // 2.放入缓存
        for (Goods goods : goodsList) {
            // 2.1将goods序列化为json
            String json = MAPPER.writeValueAsString(goods);
            // 2.2存入redis
            stringRedisTemplate.opsForValue().set("goods:id:" + goods.getId(), json);
        }
    }
}
