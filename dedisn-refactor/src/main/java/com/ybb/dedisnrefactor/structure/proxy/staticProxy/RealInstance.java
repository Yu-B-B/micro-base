package com.ybb.dedisnrefactor.structure.proxy.staticProxy;

public class RealInstance implements Subscribe{
    @Override
    public int save() {
        System.out.println("业务层执行保存方法");
        return 1;
    }
}
