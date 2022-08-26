package com.zzmhome.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration //表明该类是配置类
@EnableOpenApi
public class SwaggerConfig {
	@Bean
	public Docket docket(){//自定义首页属性Docket配置
		return new Docket(DocumentationType.OAS_30).apiInfo(
				new ApiInfoBuilder()
						.contact(new Contact("柠檬精","http://localhost:9090/swagger-ui/index.html#/","786936663@qq.com"))
						.title("secKill项目")
						.build()
		);
	}
}
