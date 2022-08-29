package com.zzmhome.seckill.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.service.GoodsService;
import com.zzmhome.seckill.service.UserService;
import com.zzmhome.seckill.utils.MD5Util;
import com.zzmhome.seckill.vo.GoodsVo;
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
import java.util.Date;
import java.util.List;

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

    @Autowired
    private GoodsService goodsService;

    /**
     * 跳转商品页面
     * @return
     */
    @ApiOperation(value = "跳转商品页面", notes = "跳转商品页面")
    @PostMapping("/toList")
    public List<GoodsVo> toList(Model model, User user){
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
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        log.info("goodsVo: " + JSONObject.toJSONString(goodsVo));
        return goodsVo;
    }

    /**
     * 查询商品详情
     * @return
     */
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情")
    @PostMapping("/toDetail")
    public GoodsVo toDetail(Model model, User user, @RequestBody Long goodsId){
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date date = new Date();
        //秒杀状态 0秒杀未开始  1秒杀开始  2秒杀已结束
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds;
        if (date.before(startDate)){
            remainSeconds = (int) ((startDate.getTime() - date.getTime())/1000);
        }else if (date.after(endDate)){
            secKillStatus = 2;
            remainSeconds = -1;
        }else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        log.info("goodsVo: " + JSONObject.toJSONString(goodsVo));
        return goodsVo;
    }
}
