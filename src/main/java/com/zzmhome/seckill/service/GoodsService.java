package com.zzmhome.seckill.service;

import com.zzmhome.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzmhome.seckill.vo.GoodsVo;

import java.util.List;

/**
 *
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 查询秒杀商品
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 查询商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
