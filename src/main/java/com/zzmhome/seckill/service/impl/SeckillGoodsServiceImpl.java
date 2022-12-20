package com.zzmhome.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzmhome.seckill.pojo.SeckillGoods;
import com.zzmhome.seckill.service.SeckillGoodsService;
import com.zzmhome.seckill.mapper.SeckillGoodsMapper;
import lombok.val;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods>
    implements SeckillGoodsService{

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 120)
    public void updateSeckillGoods(long id, Integer seckillCount) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setId(id);
        seckillGoods.setSeckillCount(seckillCount);
        updateById(seckillGoods);
    }

    @Override
    public SeckillGoods selectSeckillGoods(long id, Integer seckillCount) {
        //获取代理对象（事务）
        SeckillGoodsService seckillGoodsService = (SeckillGoodsService) AopContext.currentProxy();
        seckillGoodsService.updateSeckillGoods(id,seckillCount);
        return getById(id);
    }
}




