package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderNacosApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(OrderNacosApplication.class,args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
