package com.ybb.dedisnrefactor.structure.brige;

import org.junit.jupiter.api.Test;

public class BrigeTest {
    @Test
    public void test1() {
        Pay wxPay = new WeixinPay(new PayByFace());
        wxPay.transfer("1212");
    }
}
