package ybb.MulityThread.service;

import ybb.MulityThread.entity.OrderEntity;

public class GoodsService {
    private static double tag = 1000;
    public void decreaseStock(OrderEntity orderEntity) throws InterruptedException {
        // 根据订单中数量扣减库存
        // 查询库存数量
//        double random = Math.random();
//        Long time = (long) (random * tag);
        Thread.sleep(2000);
        // 做库存扣减

        // 更新库存
//        random = Math.random();
//        Thread.sleep((long) (random * tag));
        Thread.sleep(2000);
    }

    public static void main(String[] args) throws InterruptedException {
        GoodsService goodsService = new GoodsService();
        goodsService.decreaseStock(null);
    }
}
