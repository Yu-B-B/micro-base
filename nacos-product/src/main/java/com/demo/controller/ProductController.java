package com.demo.controller;

import com.demo.ProductEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/product")
public class ProductController {
    @GetMapping("/{id}")
    public ProductEntity getProductInfo(@PathVariable("id") String productId, HttpServletRequest handler) {
        System.out.println("得到拦截器中设置请求头" + handler.getHeader("TOKEN"));
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productId);
        productEntity.setAmount(BigDecimal.valueOf(12123.333));
        System.out.println("执行查询商品方法");

//        try {
//            TimeUnit.SECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return productEntity;
    }
}
