package com.zzmhome.seckill.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzmhome.seckill.exception.GlobalException;
import com.zzmhome.seckill.exception.GlobalExceptionHandler;
import com.zzmhome.seckill.pojo.Order;
import com.zzmhome.seckill.pojo.SeckillMessage;
import com.zzmhome.seckill.pojo.SeckillOrder;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.rabbitmq.MQSender;
import com.zzmhome.seckill.service.GoodsService;
import com.zzmhome.seckill.service.OrderService;
import com.zzmhome.seckill.service.SeckillOrderService;
import com.zzmhome.seckill.vo.GoodsVo;
import com.zzmhome.seckill.vo.RespBean;
import com.zzmhome.seckill.vo.RespBeanEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Api(value = "秒杀管理", tags = "秒杀管理")
@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    /**
     * 秒杀商品
     * windows（32G） 15W 优化前QPS：1041.7
     *              redis  795.3
     * @return
     */
    @ApiOperation(value = "秒杀", notes = "秒杀")
    @PostMapping("/doSeckill")
    public JSONObject doSecKill(Model model, User user, Long goodsId){
        log.info("doSecKill");
        JSONObject resultObj=new JSONObject();
        if (user == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        model.addAttribute("user", user);
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null){
            resultObj.put("code",RespBeanEnum.BUY_COUNT_ERROR.getCode());
            resultObj.put("message",RespBeanEnum.BUY_COUNT_ERROR.getMessage());
            return resultObj;
        }
        //内存标记，减少redis访问
        if (EmptyStockMap.get(goodsId)){
            resultObj.put("code",RespBeanEnum.EMPTY_STOCK.getCode());
            resultObj.put("message",RespBeanEnum.EMPTY_STOCK.getMessage());
            return resultObj;
        }
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        //库存递减 （原子性）
        Long count = Optional.ofNullable(opsForValue.decrement("seckillGoods" + goodsId)).orElse(-1L);
        if (count < 0){
            EmptyStockMap.put(goodsId,true);
            //redis库存为-1，显示不好，再递增1个
            opsForValue.increment("seckillGoods" + goodsId);
            resultObj.put("code",RespBeanEnum.EMPTY_STOCK.getCode());
            resultObj.put("message",RespBeanEnum.EMPTY_STOCK.getMessage());
            return resultObj;
        }
        SeckillMessage seckillMessage = new SeckillMessage(goodsId,user);
        mqSender.sendSecKillMessage(JSONObject.toJSONString(seckillMessage));

        resultObj.put("code",RespBeanEnum.SUCCESS.getCode());
        resultObj.put("message",RespBeanEnum.SUCCESS.getMessage());
        resultObj.put("order",0);

        /*
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        log.info("goodsVo: " + JSONObject.toJSONString(goods));
        if (goods.getSeckillCount() < 1){
            resultObj.put("code",RespBeanEnum.EMPTY_STOCK.getCode());
            resultObj.put("message",RespBeanEnum.EMPTY_STOCK.getMessage());
            return resultObj;
        }
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().lambda()
//                .eq(SeckillOrder::getUserId, user.getId())
//                .eq(SeckillOrder::getGoodsId, goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        if (seckillOrder != null){
            resultObj.put("code",RespBeanEnum.BUY_COUNT_ERROR.getCode());
            resultObj.put("message",RespBeanEnum.BUY_COUNT_ERROR.getMessage());
            return resultObj;
        }
        Order order = orderService.secKill(user, goods);

        resultObj.put("code",RespBeanEnum.SUCCESS.getCode());
        resultObj.put("message",RespBeanEnum.SUCCESS.getMessage());
        resultObj.put("order",order);
        */

        return resultObj;
    }

    /**
     * 获取结果
     * orderId:成功， -1：秒杀失败， 0：排队中
     *
     * @return
     */
    @ApiOperation(value = "获取结果", notes = "获取结果")
    @PostMapping("/getResult")
    public RespBean getResult(User user, Long goodsId){
        if (user == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        Long result = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(result);
    }

    /**
     * 系统初始化，把商品库存加载到redis
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVo)){
            return;
        }
        goodsVo.forEach(goods -> {
            redisTemplate.opsForValue().set("seckillGoods" + goods.getId(), goods.getSeckillCount());
            EmptyStockMap.put(goods.getId(),false);
        });
    }
}
