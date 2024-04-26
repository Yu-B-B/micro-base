package com.luke;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }


    /*
    * 使用负载均衡
    * 调用其他服务时使用负载均衡的方式进行调用
    *
    * 由LoadBalancerInterceptor实现
    * 其中实现了ClientHttpRequestInterceptor接口，用于拦截用户的所有请求
    *
    * LoadBalancerInterceptor 入口方法为intercept
    * 通过choose方法，使用轮询的方式获取需要访问的实例
    *
    * 在BlockingLoadBalancerClient中通过getInstances获取了实例对象
    * 调用choose方法
    *
    * RoundRobinLoadbalancer.choose()
    * .getIfAvailable(),获取当前实例的所有对象
    *
    * */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /*
    * Ribbon的默认规则为轮询，其他方式包括随机，示例如下
    * 或通过 配置文件 的方式配置 负载规则
    * ribbon:
    *   NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
    * */
//    @Bean
    public IRule randomRule(){
        return new RandomRule();
    }

    /*
    * ribbon默认为懒加载，在服务启动中不会加载ribbon且不会在feign中拉取相应的服务
    * 当第一次访问时才会创建LoadBalancerClient对象
    * 所以在业务请求中，第一次请求的时间远远长于后续的请求
    *
    * 解决方法：
    * 1. 使用饥饿加载，通过配置文件配置
    * ribbon:
    *  eager-load:
    *  enabled: true
    *  clients: client1  // 指定对那个或哪些服务开启饥饿加载
    *       - client1
    *       - client2
    * */
}
