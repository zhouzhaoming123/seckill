package com.zzmhome.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),

    LOGIN_ERROR(500000,"用户名或密码不正确"),
    MOBILE_ERROR(500001,"手机号码格式不正确"),
    BIND_EXCEPTION(500002,"参数校验异常"),
    SESSION_ERROR(500003,"没有用户"),

    EMPTY_STOCK(500500,"库存不足"),
    BUY_COUNT_ERROR(500501,"该商品每人限购一件"),
    REQUEST_ERROR(500502,"请求非法，请重试"),
    CAPTCHA_ERROR(500503,"验证码错误，请重输"),
    REQUEST_COUNT_ERROR(500503,"访问过于频繁，请稍后再试")
    ;

    private final Integer code;
    private final String message;
}
