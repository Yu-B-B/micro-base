package com.ybb.dedisnrefactor.createType.single;

/**
 * 使用内部类创建实例
 * 特性：
 *  1、外部类不影响内部类
 *  2、JVM只会加载一边对象
 */
public class InnerClass {
    private static class SingleClass {
        private static SingleClass instance = new SingleClass();
    }

    private InnerClass() {
    }

    public static SingleClass getInstance() {
        return SingleClass.instance;
    }
}
