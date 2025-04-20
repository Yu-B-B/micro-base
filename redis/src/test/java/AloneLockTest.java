import com.luke.lock.AloneLock;

public class AloneLockTest {
    static AloneLock lock = new AloneLock();

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " 尝试获取锁...");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 获取到锁，执行任务...");
                Thread.sleep(1000); // 模拟业务
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " 释放锁。");
            }
        };

        // 启动多个线程，模拟竞争
        for (int i = 0; i < 5; i++) {
            new Thread(task, "线程-" + i).start();
        }
    }
}
