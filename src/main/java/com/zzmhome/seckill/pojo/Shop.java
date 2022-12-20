package com.zzmhome.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

    private Long id;

    private String shopName;

    private Long typeId;

    /**
     * 经度
     */
    private Double x;

    /**
     * 纬度
     */
    private Double y;


}
