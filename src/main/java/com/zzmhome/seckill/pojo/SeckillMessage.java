package com.zzmhome.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀信息
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {

    private Long goodsId;
    private User user;

}
