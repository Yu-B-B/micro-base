package com.demo.controller;

import com.demo.ProductEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/product")
public class ProductController {
    @GetMapping("/{id}")
    public ProductEntity getProductInfo(@PathVariable("id") String productId){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productId);
        productEntity.setAmount(BigDecimal.valueOf(12123.333));
        return productEntity;
    }
}
