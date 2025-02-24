package com.demo.server;

import com.demo.OrderEntity;
import com.demo.ProductEntity;
import com.demo.feign.ProductFeign;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class OrderServer {
    @Autowired
    private DiscoveryClient discoveryClient; // 获取到某一个微服务的所有实例
    @Autowired
    private LoadBalancerClient balance;
    @Autowired
    private RestTemplate restTemplate;
//    @Autowired
//    private ProductFeign productFeign;

    public OrderEntity createOrder(String userId, String productId) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId("");
        orderEntity.setUserId(userId);
        // TODO:远程调用获取商品列表
        ProductEntity product = getProductFromRemote(productId);
        System.out.println("调用 ----- ");

        orderEntity.setNum(3);
        orderEntity.setAmount(BigDecimal.valueOf(orderEntity.getNum()).multiply(product.getAmount()));
        orderEntity.setProductList(Arrays.asList(product));
        orderEntity.setAmount(BigDecimal.valueOf(133.222));
        orderEntity.setAddress("");
        orderEntity.setCreateTime(new Date());
        return orderEntity;
    }

    public ProductEntity getProductFromRemote(String productId) {
        // 方式1，获取运城服务的IP+端口列表
        //List<ServiceInstance> instances = discoveryClient.getInstances("nacos-product");
        //ServiceInstance instance = instances.get(0);

        // 方式2,采用负载均衡，默认采用轮询的方式
        ServiceInstance instance = balance.choose("nacos-product");

        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/product/" + productId;

        // 方式3：采用注解方式将请求设置为负载均衡 @LoadBalanced
        url = "http://nacos-product/product/" + productId;

        // 使用RestTemplate 远程调用，RestTemplate 为线程安全的，抽取为工具类
        return restTemplate.getForObject(url, ProductEntity.class);

//         方式4：使用OpenFeign远程获取商品信息
//        System.out.println("调用 ----- ");
//        return productFeign.getProduct(productId);

    }
}
