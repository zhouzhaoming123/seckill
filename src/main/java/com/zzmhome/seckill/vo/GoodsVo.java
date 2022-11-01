package com.zzmhome.seckill.vo;

import com.zzmhome.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品返回对象
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {

    private BigDecimal seckillPrice;

    private Integer seckillCount;

    private Date startDate;

    private Date endDate;

}
