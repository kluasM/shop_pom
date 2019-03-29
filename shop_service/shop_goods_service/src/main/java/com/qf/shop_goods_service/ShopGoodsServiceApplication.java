package com.qf.shop_goods_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.qf")
@MapperScan("com.qf.dao")
@DubboComponentScan("com.qf.serviceImpl")
public class ShopGoodsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopGoodsServiceApplication.class, args);
	}
	@Bean
	public Queue getQueue(){
		return new Queue("goods_queue");
	}
	@Bean
	public FanoutExchange getExchange(){
		return new FanoutExchange("goods_exchange");
	}
	@Bean
	public Binding getExchageBinding(Queue getQueue,FanoutExchange getExchange){
		return BindingBuilder.bind(getQueue).to(getExchange);
	}
}

