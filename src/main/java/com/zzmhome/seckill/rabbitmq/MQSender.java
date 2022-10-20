package com.zzmhome.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg){
        log.info("发送消息：" + msg);
        rabbitTemplate.convertAndSend("queue",msg);
    }

    public void sendFanOut(Object msg){
        log.info("发送消息：" + msg);
        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
    }

    public void sendDirect01(Object msg){
        log.info("发送red消息：" + msg);
        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
    }

    public void sendDirect02(Object msg){
        log.info("发送green消息：" + msg);
        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
    }

    public void sendTopic01(Object msg){
        log.info("发送Topic01消息(QUEUE01接收)：" + msg);
        rabbitTemplate.convertAndSend("topicExchange","queue.red.message",msg);
    }

    public void sendTopic02(Object msg){
        log.info("发送Topic02消息(被两个QUEUE接收)：" + msg);
        rabbitTemplate.convertAndSend("topicExchange","message.queue.green.abc",msg);
    }

}
