package com.demo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductEntity {
    private String productId;
    private BigDecimal amount;

}
