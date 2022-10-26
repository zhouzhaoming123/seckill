package com.zzmhome.seckill.service;

import com.zzmhome.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzmhome.seckill.pojo.User;

/**
 *
 */
public interface SeckillOrderService extends IService<SeckillOrder> {

    /**
     * orderId:成功， -1：秒杀失败， 0：排队中
     * @param user
     * @param goodsId
     * @return
     */
    Long getResult(User user, Long goodsId);
}
