package com.ybb.dedisnrefactor.createType.single;

/**
 * 饿汉模式
 * 类初始化过程中创建对象，获取对象时更快
 * 若当前实例长时间不适用，造成内存浪费。
 */
public class HungryClass {
    public HungryClass() {
    }

    private static HungryClass instance = new HungryClass();

    public static HungryClass getInstance() {
        return instance;
    }
}
