package com.luke.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("getUser")
    public void getUserService(){
        String url = "http://user-server/user/getUser";
        String str = restTemplate.getForObject(url,String.class);
        System.out.println(str);
    }
}
