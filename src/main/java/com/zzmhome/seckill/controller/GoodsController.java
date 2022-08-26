package com.zzmhome.seckill.controller;

import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.service.UserService;
import com.zzmhome.seckill.utils.MD5Util;
import com.zzmhome.seckill.vo.LoginVo;
import com.zzmhome.seckill.vo.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 商品
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Api(value = "商品管理", tags = "商品管理")
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private UserService userService;

    /**
     * 跳转商品页面
     * @return
     */
    @ApiOperation(value = "跳转商品页面", notes = "跳转商品页面")
    @PostMapping("/toList")
    public String toList(Model model, User user){
//        if (StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        //User user = (User)session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket,request,response);
//        log.info("{}",user);
//        if (user == null){
//            return "login";
//        }
        model.addAttribute("user", user);
        return "goodsList";
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
