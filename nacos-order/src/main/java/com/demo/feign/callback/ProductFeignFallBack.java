package com.demo.feign.callback;

import com.demo.ProductEntity;
import com.demo.feign.ProductFeign;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductFeignFallBack implements ProductFeign {
    @Override
    public ProductEntity getProduct(String id) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId("1kdsajf112");
        productEntity.setAmount(BigDecimal.valueOf(12));
        return productEntity;
    }
}
