package com.zzmhome.seckill.service;

import com.zzmhome.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzmhome.seckill.vo.LoginVo;
import com.zzmhome.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public interface UserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
