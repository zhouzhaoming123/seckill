package com.zzmhome.seckill.config;

import com.zzmhome.seckill.pojo.User;
import org.springframework.stereotype.Component;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }

    public static void removeUser(){
        userHolder.remove();
    }

}


