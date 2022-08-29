package com.zzmhome.seckill.service;

import com.zzmhome.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.vo.GoodsVo;

/**
 *
 */
public interface OrderService extends IService<Order> {

    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order secKill(User user, GoodsVo goods);
}
