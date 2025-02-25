package com.demo.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderConfig {
    // openFeign重试器
    @Bean
    Retryer retryer(){
        // this(100L, TimeUnit.SECONDS.toMillis(1L), 5); // 从100ms开始，每次重试为上次时间的1.5倍，最大时间为1s，最大重试5次
        return new Retryer.Default(); // Default 包括 重试间隔时间，最大超时时间，最大重试次数
    }

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
