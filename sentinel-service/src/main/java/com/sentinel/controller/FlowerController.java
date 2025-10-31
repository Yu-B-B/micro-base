package com.sentinel.controller;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘接口访问
 */
@RestController
public class FlowerController {
    @GetMapping("flow1")
    public String flowMethod1() {
        try{
            Thread.sleep(4000);
            return "A";
        }catch (Exception e) {
            throw new RuntimeException("运行超时");
        }
    }

    @GetMapping("flow2")
    public String flowMethod2() {
        return "B";
    }

    // ========= 热点规则 =========
    @GetMapping("hotKey")
    @SentinelResource(value = "hotKey", blockHandler = "hotKey_bck")
    public String hotKey(@RequestParam(value = "hot1",required = false) String key1,
                         @RequestParam(value = "hot2",required = false) String key2,
                         @RequestParam(value = "hot3",required = false) String key3) {
        return "hotKey Test";
    }

    public String hotKey_bck(String hot1, String hot2, String hot3, BlockException handler) {
        return "请稍后重试...";
    }
}
