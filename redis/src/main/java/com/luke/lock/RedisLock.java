package com.luke.lock;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class RedisLock implements Lock {

    private final static int LOCK_WAIT_TIME = 5*1000; // 5s
    private final static String NS = "ss";

    private final static String LUA = "" +
            "if redis call('get',KEYS[1]) == ARGV[1] THEN \n" +
            "RETURN REDIS.CALL('DEL',KEYS[1])\n" +
            "ELSE RETURN 0 END";

    private ThreadLocal<String> lockerId = new ThreadLocal<>();

    // 处理锁重入问题
    private Thread ownerThread;
    private String lockName = "lock";

//    @Autowired
//    private JedisPool jedisPool;

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

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
