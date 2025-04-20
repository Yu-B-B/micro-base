package com.luke.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("getUser")
    public String getUser(){
        return "user2";
    }
}
