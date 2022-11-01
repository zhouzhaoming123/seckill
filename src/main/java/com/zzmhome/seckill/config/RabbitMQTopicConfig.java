package com.zzmhome.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类-Direct
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Configuration
public class RabbitMQTopicConfig {

//    private static final String QUEUE01 = "queue_topic01";
//    private static final String QUEUE02 = "queue_topic02";
//    private static final String EXCHANGE = "topicExchange";
//    private static final String ROUTINGKEY01 = "#.queue.#";
//    private static final String ROUTINGKEY02 = "*.queue.#";
//
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY02);
//    }

    private static final String QUEUE = "queue_seckill";
    private static final String EXCHANGE = "exchange_seckill";
    private static final String ROUTINGKEY = "seckill.#";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with(ROUTINGKEY);
    }

}
