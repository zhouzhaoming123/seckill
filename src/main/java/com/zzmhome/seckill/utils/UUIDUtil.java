package com.zzmhome.seckill.utils;

import java.util.UUID;

/**
 * UUID工具类
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
