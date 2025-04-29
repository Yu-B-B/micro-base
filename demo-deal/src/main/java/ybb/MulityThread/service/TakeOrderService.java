package ybb.MulityThread.service;

import ybb.MulityThread.entity.OrderEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TakeOrderService {

    public static CountDownLatch countDownLatch = new CountDownLatch(1000);

    public static void main(String[] args) throws Exception {
        TakeOrderService takeOrderService = new TakeOrderService();
//        takeOrderService.createOrder(111L);
//        takeOrderService.createOrderV1(111L);

        // v2
        for (int i = 0; i < 1000; i++) {
            final long id = i;
            Thread thread = new Thread(() -> {
                try {
                    countDownLatch.await();
                    takeOrderService.createOrderV2(id);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            );
            countDownLatch.countDown();
            thread.start();
        }
    }

    /**
     * 创建订单时，通过订单数据还需要做库存扣减、积分累加、发货等操作
     * 这三个接口属于三个子任务
     * 且三个子任务为串行化调用
     * <p>
     * v1 - 因为子任务执行前需要有Order中的数据，子任务依赖于order，所以启动三个子线程
     * 缩短子任务的执行时间
     * <p>
     * v2 - 当订单数据请求大时，交给后面子任务的线程数量也会增大。原始方式只会让订单服务增加并发压力，从而将请求分发到子任务中
     * 采用子任务后，压力将转移到子任务中
     *
     * @param id
     * @throws InterruptedException
     */
    public void createOrder(Long id) throws Exception {
        long start = System.currentTimeMillis();
        OrderService orderService = new OrderService();
        OrderEntity order = orderService.createOrder(id);
        GoodsService goodsService = new GoodsService();
        goodsService.decreaseStock(order);
        PointService pointService = new PointService();
        pointService.addPoint(order);
        long end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start) + "ms");
    }

    // v1
    public void createOrderV1(Long id) throws Exception {
        long start = System.currentTimeMillis();
        PointService pointService = new PointService();
        GoodsService goodsService = new GoodsService();
        OrderService orderService = new OrderService();

        OrderEntity order = orderService.createOrder(id);

        Callable<Object> callable1 = new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                goodsService.decreaseStock(order);

                return null;
            }
        };


        Callable<Object> callable2 = new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                pointService.addPoint(order);
                return null;
            }
        };

        FutureTask<Object> task1 = new FutureTask<Object>(callable1);
        FutureTask<Object> task2 = new FutureTask<Object>(callable1);

        new Thread(task1).start();
        new Thread(task2).start();

        // 获取结构
        task1.get();
        task2.get();

        long end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start) + "ms");
    }

    // v2 - 拿到订单信息后写入消息队列中，采用定时任务批量获取订单数据，将批量数据交给子任务，子任务执行后返回批量结果做分发
    // 批量测试时，调用当前方法
    public void createOrderV2(Long id) throws Exception {
        long start = System.currentTimeMillis();
        OrderService orderService = new OrderService();
        OrderEntity order = orderService.createOrder(id);

        CompletableFuture<Object> future = new CompletableFuture<>();
        Request request = new Request();
        request.future = future;
        request.obj = order;

        orderList.add(request);

        future.get(); // 异步获取结果
    }

    // 采用JUC中结构模仿消息队列，该对象中应该存储Callable泛型
    LinkedBlockingQueue<Request> orderList = new LinkedBlockingQueue<>();

    class Request {
        Object obj;
        CompletableFuture<Object> future;
    }

    // 定时任务执行
    @PostConstruct
    public void doMethod() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        PointService pointService = new PointService();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int size = orderList.size();
                if (size == 0) return; // 产生数据读取不均匀，可做强制数量调整
                List<Object> dataList = new ArrayList<>();
                List<Request> requestList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Request itemData = orderList.poll();
                    requestList.add(itemData);
                    dataList.add(itemData.obj);
                }
                try {
                    List<Object> responseData = pointService.addPointBatch(dataList);
                    // 分发结果

                    System.out.println("调用次数" + responseData.size());
                    for (Object response : responseData) {
                        for (Request request : requestList) {
                            // 判断response中id与request中order是否一致
                            // 若一致，拿到返回结果
                            request.future.complete(response);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, 50, TimeUnit.MILLISECONDS);
    }

    // v3 - 批处理结合MYSQL综合优化
    /**
    * 扣减库存时，将扣减数量与商品id 存储到HashMap中，采用一条sql做数据的批量扣减
    * */



    /**
     * FutureTask原理
     *
     * FutureTask底层实现了Runnable与Future接口，所以可以交给Thread运行
     * */
}
