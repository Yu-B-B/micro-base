package com.demo.feign;

import com.demo.ProductEntity;
import com.demo.feign.callback.ProductFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="nacos-product",fallback = ProductFeignFallBack.class) // 声明远程调用的那个服务
public interface ProductFeign {
    @GetMapping("/api/product/{id}")
    ProductEntity getProduct(@PathVariable("id") String id);
}
