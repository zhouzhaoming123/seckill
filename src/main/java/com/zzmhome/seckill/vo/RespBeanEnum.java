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
    BIND_EXCEPTION(500002,"参数校验异常")
    ;

    private final Integer code;
    private final String message;
}
