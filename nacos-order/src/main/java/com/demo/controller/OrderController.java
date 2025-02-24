package com.demo.controller;

import com.demo.OrderEntity;
import com.demo.nacosConfig.DefaultProperties;
import com.demo.server.OrderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderServer orderServer;
    @Autowired

    private DefaultProperties defaultProperties;
    @GetMapping("/{userId}/{productId}")
    public OrderEntity createOrder(@PathVariable("userId")String userId,@PathVariable("productId")String productId){
        OrderEntity order = orderServer.createOrder(userId, productId);
        System.out.println(defaultProperties.getPort());
        System.out.println(defaultProperties.getName());
        return order;
    }
}
