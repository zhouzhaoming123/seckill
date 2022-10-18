package com.zzmhome.seckill.controller;

import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    @PostMapping
    public RespBean info(User user){
        return RespBean.success(user);
    }
}
