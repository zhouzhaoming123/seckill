package com.zzmhome.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzmhome.seckill.pojo.SeckillOrder;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.service.SeckillOrderService;
import com.zzmhome.seckill.mapper.SeckillOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder>
    implements SeckillOrderService{

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null){
            return seckillOrder.getOrderId();
        }else if(Optional.ofNullable(redisTemplate.hasKey("isStockEmpty:" + goodsId)).orElse(false)){
            return -1L;
        }
        return 0L;
    }
}




