package com.ybb.dedisnrefactor.createType.single;

/**
 * 懒汉模式-双检锁
 */
public class LazyClassDcl {
    private volatile static LazyClassDcl instance = null;

    public LazyClassDcl() {

    }

    public static LazyClassDcl getInstance() {
        if (instance == null) {
            synchronized (LazyClassDcl.class) {
                if (instance == null) {
                    instance = new LazyClassDcl();
                }
            }
        }
        return instance;
    }
}
