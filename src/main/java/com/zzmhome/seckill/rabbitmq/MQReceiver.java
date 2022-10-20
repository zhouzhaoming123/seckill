package com.zzmhome.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_fanout01")
    public void receive01(Object msg){
        log.info("queue_fanout01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receive02(Object msg){
        log.info("queue_fanout02接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_direct01")
    public void receiveDirect01(Object msg){
        log.info("queue_direct01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void receiveDirect02(Object msg){
        log.info("queue_direct02接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_topic01")
    public void receiveTopic01(Object msg){
        log.info("QUEUE01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_topic02")
    public void receiveTopic02(Object msg){
        log.info("QUEUE02接收消息：" + msg);
    }

}
