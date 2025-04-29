package ybb.MulityThread.service;

import ybb.MulityThread.entity.OrderEntity;

import java.util.List;

public class PointService {
    private static double tag = 1000;

    public void addPoint(OrderEntity order) throws Exception {
        // 根据订单中数量扣减库存
        // 查询库存数量
//        double random = Math.random();
//        Long time = (long) (random * tag);
//        Thread.sleep(time);
        Thread.sleep(2000);
        // 做库存扣减

        // 更新库存
//        random = Math.random();
//        Thread.sleep((long) (random * tag));
        Thread.sleep(2000);

    }
    public List<Object> addPointBatch(List<Object> orders) throws Exception {
        return null;
    }
}
