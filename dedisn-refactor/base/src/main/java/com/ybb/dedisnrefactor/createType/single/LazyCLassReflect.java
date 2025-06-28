package com.ybb.dedisnrefactor.createType.single;

import java.lang.reflect.Constructor;

/**
 * 懒加载，通过反射创建实例
 * <p>
 * 问题：反射创建对象时存在将访问设置为true，破坏私有性
 */
public class LazyCLassReflect {
    private static LazyCLassReflect instance = new LazyCLassReflect();

    private LazyCLassReflect() {
        // 为了解决反射创建对象不可控的问题，增加一轮判断
        // 但是没有做到对象创建简单的原则
        if (instance != null) {
            throw new RuntimeException("已经创建过了");
        }
    }

    public static LazyCLassReflect getInstance() {
        return instance;
    }
}

class ReflectTest {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = LazyCLassReflect.class;

        Constructor<?> constructor = clazz.getDeclaredConstructor(null);

        // 破坏了对象中私有性
        constructor.setAccessible(true);

        Object obj = constructor.newInstance();
        Object obj1 = constructor.newInstance();

        System.out.println(obj == obj1);
    }
}
