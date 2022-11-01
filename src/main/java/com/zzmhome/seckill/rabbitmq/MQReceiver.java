package com.zzmhome.seckill.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.zzmhome.seckill.pojo.SeckillMessage;
import com.zzmhome.seckill.pojo.SeckillOrder;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.service.GoodsService;
import com.zzmhome.seckill.service.OrderService;
import com.zzmhome.seckill.vo.GoodsVo;
import com.zzmhome.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Service
@Slf4j
public class MQReceiver {

//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("queue_fanout01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("queue_fanout02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receiveDirect01(Object msg){
//        log.info("queue_direct01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receiveDirect02(Object msg){
//        log.info("queue_direct02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receiveTopic01(Object msg){
//        log.info("QUEUE01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receiveTopic02(Object msg){
//        log.info("QUEUE02接收消息：" + msg);
//    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "queue_seckill")
    public void receiveSecKill(String message){
        log.info("秒杀消息接收：" + message);
        SeckillMessage seckillMessage = JSONObject.parseObject(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        //判断库存
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getGoodsStock() < 1){
            return;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null){
            return;
        }
        //下单操作
        orderService.secKill(user,goodsVo);

    }

}
