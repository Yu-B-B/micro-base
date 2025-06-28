package com.ybb.dedisnrefactor.structure.proxy.staticProxy;

public class Client {
    public static void main(String[] args) {
        // 真实业务
        RealInstance realInstance = new RealInstance();
        // 代理业务
        RealProxy realProxy = new RealProxy(realInstance);
        realProxy.save();
    }
}
