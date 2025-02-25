package com.demo;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableFeignClients
public class OrderNacosApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(OrderNacosApplication.class,args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    ApplicationRunner runner(NacosConfigManager manager) throws NacosException {
        return args -> { // 简写后
            // 获取配置服务
            ConfigService service = manager.getConfigService();
            // 获取指定配置
            service.addListener("nacos_order.yaml", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);
                }

                @Override
                public void receiveConfigInfo(String content) {
                    System.out.println("发生变化的配置："+content);
                }
            });
        };
    }
}
