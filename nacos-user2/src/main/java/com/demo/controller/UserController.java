package com.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class UserController {
    @Value("${demo.name}")
    private String name;
    @GetMapping("/test")
    public String test() {
        return "8090";
//        return name;
    }

}
