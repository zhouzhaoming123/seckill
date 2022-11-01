package com.zzmhome.seckill.mapper;

import com.zzmhome.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzmhome.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.zzmhome.seckill.pojo.Goods
 */
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
}




