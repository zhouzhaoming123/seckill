package com.zzmhome.seckill.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码校验
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public class ValidatorUtil {

    private static final Pattern mobile_patten = Pattern.compile("^1[34568]\\d{9}$");

    public static boolean isMobile(String mobile){
        if (StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = mobile_patten.matcher(mobile);
        return matcher.matches();
    }

}
