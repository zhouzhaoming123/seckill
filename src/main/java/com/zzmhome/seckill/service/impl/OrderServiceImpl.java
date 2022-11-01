package com.zzmhome.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzmhome.seckill.pojo.Order;
import com.zzmhome.seckill.pojo.SeckillGoods;
import com.zzmhome.seckill.pojo.SeckillOrder;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.service.OrderService;
import com.zzmhome.seckill.mapper.OrderMapper;
import com.zzmhome.seckill.service.SeckillGoodsService;
import com.zzmhome.seckill.service.SeckillOrderService;
import com.zzmhome.seckill.utils.MD5Util;
import com.zzmhome.seckill.utils.UUIDUtil;
import com.zzmhome.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @Transactional
    public Order secKill(User user, GoodsVo goods) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().lambda()
                .eq(SeckillGoods::getGoodsId, goods.getId()));
        seckillGoods.setSeckillCount(seckillGoods.getSeckillCount() - 1);
//        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("seckill_count", seckillGoods.getSeckillCount())
//                .eq("id", seckillGoods.getId()).gt("seckill_count", 0));
//        seckillGoodsService.updateById(seckillGoods);

        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().
                setSql("seckill_count=seckill_count-1").eq("goods_id",goods.getId()).gt("seckill_count", 0));

        if (seckillGoods.getSeckillCount() < 1){
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goods.getId(),"0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user==null || goodsId<0 || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String)redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user==null || goodsId<0 || StringUtils.isEmpty(captcha)){
            return false;
        }
        String redisCaptcha = (String)redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}




