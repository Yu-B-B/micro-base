package com.demo.feign;

import com.demo.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="nacos-product") // 声明远程调用的那个服务
public interface ProductFeign {
    @GetMapping("/product/{id}")
    ProductEntity getProduct(@PathVariable("id") String id);
}
