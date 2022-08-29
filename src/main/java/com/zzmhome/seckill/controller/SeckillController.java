package com.zzmhome.seckill.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzmhome.seckill.exception.GlobalException;
import com.zzmhome.seckill.exception.GlobalExceptionHandler;
import com.zzmhome.seckill.pojo.Order;
import com.zzmhome.seckill.pojo.SeckillOrder;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.service.GoodsService;
import com.zzmhome.seckill.service.OrderService;
import com.zzmhome.seckill.service.SeckillOrderService;
import com.zzmhome.seckill.vo.GoodsVo;
import com.zzmhome.seckill.vo.RespBean;
import com.zzmhome.seckill.vo.RespBeanEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Api(value = "秒杀管理", tags = "秒杀管理")
@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

    /**
     * 跳转商品页面
     * @return
     */
    @ApiOperation(value = "秒杀", notes = "秒杀")
    @PostMapping("/doSeckill")
    public JSONObject toList(Model model, User user, @RequestBody Long goodsId){
        JSONObject resultObj=new JSONObject();

        if (user == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        log.info("goodsVo: " + JSONObject.toJSONString(goods));
        if (goods.getSeckillCount() < 1){
            resultObj.put("code",RespBeanEnum.EMPTY_STOCK.getCode());
            resultObj.put("message",RespBeanEnum.EMPTY_STOCK.getMessage());
            return resultObj;
        }
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().lambda()
                .eq(SeckillOrder::getUserId, user.getId())
                .eq(SeckillOrder::getGoodsId, goodsId));
        if (seckillOrder != null){
            resultObj.put("code",RespBeanEnum.BUY_COUNT_ERROR.getCode());
            resultObj.put("message",RespBeanEnum.BUY_COUNT_ERROR.getMessage());
            return resultObj;
        }
        Order order = orderService.secKill(user, goods);

        resultObj.put("code",RespBeanEnum.SUCCESS.getCode());
        resultObj.put("message",RespBeanEnum.SUCCESS.getMessage());
        resultObj.put("order",order);

        return resultObj;
    }
}
