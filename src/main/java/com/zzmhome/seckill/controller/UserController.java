package com.zzmhome.seckill.controller;

import com.zzmhome.seckill.pojo.User;
import com.zzmhome.seckill.rabbitmq.MQSender;
import com.zzmhome.seckill.vo.MQRequest;
import com.zzmhome.seckill.vo.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Api(value = "用户管理", tags = "用户管理")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    @ApiOperation(value = "用户", notes = "用户")
    @PostMapping("/info")
    public RespBean info(User user){
        return RespBean.success(user);
    }

    /**
     * 测试发送RabbitMQ消息
     * @param str
     */
    @ApiOperation(value = "测试MQ", notes = "测试MQ")
    @PostMapping("/mq")
    public void mq(String str){
        mqSender.send(str);
    }

    @ApiOperation(value = "fanout测试MQ", notes = "fanout测试MQ")
    @PostMapping("/mq/fanout")
    public void mqFanout(String str){
        mqSender.sendFanOut(str);
    }

    @ApiOperation(value = "direct测试MQ", notes = "direct测试MQ")
    @PostMapping("/mq/direct")
    public void mqDirect(@RequestBody MQRequest mqRequest){
        mqSender.sendDirect01(mqRequest.getMsg01());
        mqSender.sendDirect02(mqRequest.getMsg02());
    }

    @ApiOperation(value = "topic测试MQ", notes = "topic测试MQ")
    @PostMapping("/mq/topic")
    public void mqTopic(@RequestBody MQRequest mqRequest){
        mqSender.sendTopic01(mqRequest.getMsg01());
        mqSender.sendTopic02(mqRequest.getMsg02());
    }


}
