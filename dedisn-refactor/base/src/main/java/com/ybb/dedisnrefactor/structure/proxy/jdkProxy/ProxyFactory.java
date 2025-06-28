package com.ybb.dedisnrefactor.structure.proxy.jdkProxy;

import org.aopalliance.intercept.Invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
    private Object realProxy;

    public ProxyFactory(Object realProxy) {
        this.realProxy = realProxy;
    }

    public Object getProxyInstance(){
        return Proxy.newProxyInstance(
                // 类加载器
                realProxy.getClass().getClassLoader(),
                // 代理的接口
                realProxy.getClass().getInterfaces(),
                // 事件处理器
                new InvocationHandler() {
                    /**
                     * @param proxy  代理的真实对象
                     * @param method 代理对象中要调用的方法
                     * @param args   代理对象方法中需要的参数
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("事务开启");
                        method.invoke(realProxy, args);
                        System.out.println("关闭事务");
                        return 1;
                    }
                }
        );
    }
}
