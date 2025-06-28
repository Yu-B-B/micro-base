package com.ybb.dedisnrefactor.structure.proxy.staticProxy;

public class RealProxy implements Subscribe {
    private RealInstance realInstance;

    public RealProxy(RealInstance realInstance) {
        this.realInstance = realInstance;
    }

    @Override
    public int save() {
        System.out.println("代理层执行前");
        // 执行【真实业务】中方法
        realInstance.save();
        System.out.println("代理层执行后");
        return 0;
    }
}
