package com.ybb.dedisnrefactor.createType.single;

/**
 * 懒汉模式-加锁解决并发
 * 但是调用方法变为串行化，并发程度变为1
 */
public class LazyClassSync {
    private static LazyClassSync instance = null;

    public LazyClassSync() {
    }

    public static synchronized LazyClassSync getInstance() {
        if (instance == null) {
            instance = new LazyClassSync();
        }
        return instance;
    }
}
