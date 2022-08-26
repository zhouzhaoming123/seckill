package com.zzmhome.seckill.controller;

import com.zzmhome.seckill.service.UserService;
import com.zzmhome.seckill.utils.MD5Util;
import com.zzmhome.seckill.vo.LoginVo;
import com.zzmhome.seckill.vo.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Api(value = "用户登录管理", tags = "用户登录管理")
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转登录页面
     * @return
     */
    @ApiOperation(value = "跳转登录页面", notes = "跳转登录页面")
    @PostMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info("{}", loginVo);
        loginVo.setPassword(MD5Util.inputPassToFromPass(loginVo.getPassword()));
        return userService.doLogin(loginVo, request, response);
    }

}
