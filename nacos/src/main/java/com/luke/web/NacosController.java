package com.luke.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/nacos")
@RefreshScope
public class NacosController {
    @Value("${spring.data:}")
    private String springData;
    @GetMapping("/getProperties")
    public String getNacosConfigProperties(){
        System.out.println(springData);
        return springData;
    }
    @GetMapping("/getDefault")
    public String getDefault(){
        return "123123";
    }
}
