package com.sentinel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘接口访问
 */
@RestController
public class FlowerController {
    @GetMapping("flow1")
    public String flowMethod1() {
        return "A";
    }

    @GetMapping("flow2")
    public String flowMethod2() {
        return "B";
    }
}
