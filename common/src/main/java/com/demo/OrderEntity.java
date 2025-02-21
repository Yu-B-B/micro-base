package com.demo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderEntity {
    private String orderId;
    private String userId;
    private List<ProductEntity> productList;
    private int num;
    private BigDecimal amount;
    private String address;
    private Date createTime;
}
