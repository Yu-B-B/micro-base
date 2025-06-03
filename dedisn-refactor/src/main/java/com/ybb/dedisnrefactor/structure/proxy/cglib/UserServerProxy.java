package com.ybb.dedisnrefactor.structure.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class UserServerProxy implements MethodInterceptor {

    public Object increase(Object target){
        // 增强创建类，给所有public且非final修饰的方法增加拦截逻辑
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setSuperclass(target.getClass());
        // 回调
        enhancer.setCallback(this);

        return enhancer.create();
    }

    /**
     * @param o             代理对象
     * @param method        目标类中需要代理的方法
     * @param objects       方法中参数
     * @param methodProxy   代理对象中方法
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        // 目标对象中的方法
        System.out.println("目标对象需要代理的方法"+method.getName());
        if(method.getName().equals("strongDetail")){
            System.out.println("--当前调用方法是细节隐藏，准备开始做内部增强--");
            System.out.println("开始内部增强");
        }
        // 代理对象调用方法，这里要调用父类代理方法，如果走自己，将会出现无限递归（自己一直代理自己）
        result = methodProxy.invokeSuper(o, objects);
        // after do
        System.out.println("日志记录");
        return result;
    }
}
