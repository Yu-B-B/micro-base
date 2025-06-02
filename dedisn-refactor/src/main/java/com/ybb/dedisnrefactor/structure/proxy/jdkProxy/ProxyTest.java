package com.ybb.dedisnrefactor.structure.proxy.jdkProxy;

import com.ybb.dedisnrefactor.structure.proxy.staticProxy.RealInstance;
import com.ybb.dedisnrefactor.structure.proxy.staticProxy.Subscribe;
import org.junit.jupiter.api.Test;

public class ProxyTest {
    @Test
    public void testProxy() {
        Subscribe target = new RealInstance();

        ProxyFactory factory = new ProxyFactory(target);
        Subscribe instance = (Subscribe) factory.getProxyInstance();

        instance.save();
    }
}
