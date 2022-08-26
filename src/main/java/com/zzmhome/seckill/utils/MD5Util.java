package com.zzmhome.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public class MD5Util {

    private static final String SALT = "1q2w3e4r";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFromPass(String inputPass){
        String str = SALT.charAt(0) + SALT.charAt(3) + inputPass + SALT.charAt(7) + SALT.charAt(2);
        return md5(str);
    }

    public static String fromPassToDbPass(String fromPass, String salt){
        String str = salt.charAt(0) + salt.charAt(3) + fromPass + salt.charAt(7) + salt.charAt(2);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDbPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        //ffc8f4779261f7986c1e978591304a84
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(fromPassToDbPass("ffc8f4779261f7986c1e978591304a84","1a2b3c4d"));
        System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));
    }
}


