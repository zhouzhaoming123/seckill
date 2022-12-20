package com.zzmhome.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 获取代理对象（事务）注解
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@MapperScan("com.zzmhome.seckill.mapper")
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

}
