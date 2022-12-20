package com.zzmhome.seckill.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * redis逻辑过期对象
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
