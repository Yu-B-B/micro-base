package com.luke.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/* 单机锁 */
public class AloneLock implements Lock {
    AtomicReference<Thread> owner = new AtomicReference<>();
    // 获取锁失败需要等待的线程
    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    @Override
    public void lock() {
        // 记录哪个线程拿到锁了，使用AtomicReference记录
        while (!owner.compareAndSet(null, Thread.currentThread())) {
            waiters.add(Thread.currentThread()); // 不是当前线程的内容进入等待队列
            LockSupport.park(); // 让当前线程阻塞
            waiters.remove(Thread.currentThread()); // 解锁后从等待队列中删除
        }
    }

    @Override
    public void unlock() {
        if (owner.compareAndSet(null, Thread.currentThread())) {
            for (Thread t : waiters) {
                LockSupport.unpark(t); // 解锁当前线程的所有线程组
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
