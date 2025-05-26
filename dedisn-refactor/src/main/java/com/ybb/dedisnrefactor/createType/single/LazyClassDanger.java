package com.ybb.dedisnrefactor.createType.single;

/**
 * 懒汉模式
 * 在需要使用时去创建对象
 * 并发下容易出现多次初始化
 */
public class LazyClassDanger {
    public LazyClassDanger() {
    }

    private static LazyClassDanger instance = null;

    public static LazyClassDanger getInstance() {
        if (instance == null) {
            instance = new LazyClassDanger();
        }
        return instance;
    }
}
