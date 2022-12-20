package com.zzmhome.seckill.service;

import com.zzmhome.seckill.pojo.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface SeckillGoodsService extends IService<SeckillGoods> {

    void updateSeckillGoods(long id,Integer seckillCount);

    SeckillGoods selectSeckillGoods(long id,Integer seckillCount);

}
