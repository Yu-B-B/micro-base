package ybb.MulityThread.service;

import ybb.MulityThread.entity.OrderEntity;

import java.time.LocalDateTime;

public class OrderService {
    public OrderEntity createOrder(Long id) {
        OrderEntity entity = new OrderEntity();
        entity.setId(id);
        entity.setAddress("四川省成都市成华区");
        entity.setOrderNo("123124124L");
        entity.setCreateTime(LocalDateTime.now());
        entity.setName("王老吴");
        return entity;
    }
}
