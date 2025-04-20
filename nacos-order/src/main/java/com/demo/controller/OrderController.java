package com.demo.controller;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.demo.OrderEntity;
import com.demo.nacosConfig.DefaultProperties;
import com.demo.server.OrderServer;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderServer orderServer;
    @Autowired
    private DefaultProperties defaultProperties;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/create")
    public OrderEntity createOrder(@RequestParam("userId") String userId, @RequestParam("productId") String productId) {
        OrderEntity order = orderServer.createOrder(userId, productId);
        return order;
    }

    @GetMapping("/kill")
    @SentinelResource(value = "kill", fallback = "killFallback") // 做热点参数限制
    public OrderEntity kill(@RequestParam(value = "userId") String userId, @RequestParam("productId") String productId) {
        OrderEntity order = orderServer.createOrder(userId, productId);
        order.setOrderId("12333333333333333333333333");
        return order;
    }

    // 热点参数限制使用到的方法
    public OrderEntity killFallback(String userId, String productId, Throwable exception) {
        OrderEntity order = orderServer.createOrder(userId, productId);
        order.setOrderId("limit Sentinel params rule");
        log.info("......热点参数兜底回调......");
        return order;
    }

    @GetMapping("/quickstart")
    public String quickstart() {
        System.out.println("quickstart");
        return "quickstart";
    }

    @GetMapping("/warmup")
    public String warmup() {
        log.info("warmup");
        return "warmup";
    }
}
