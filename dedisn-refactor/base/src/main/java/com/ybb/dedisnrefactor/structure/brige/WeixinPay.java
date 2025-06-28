package com.ybb.dedisnrefactor.structure.brige;

public class WeixinPay extends Pay{
    public WeixinPay(PayMode payMode) {
        super(payMode);
    }

    @Override
    public void transfer(String uid) {
        System.out.println("微信支付方式");
        payMode.payMethod(uid);
    }
}
