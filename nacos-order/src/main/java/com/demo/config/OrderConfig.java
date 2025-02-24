package com.demo.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderConfig {
//    @Bean
//    Retryer retryer(){
//        return new Retryer.Default(); // Default 包括 重试间隔时间，最大超时时间，最大重试次数
//    }

    // openFeign调用日志记录
    @Bean
    Logger.Level openFeignLevel(){
        return Logger.Level.FULL;
    }
    @Bean
    @LoadBalanced
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }
}
