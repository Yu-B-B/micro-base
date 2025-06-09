package com.ybb.dedisnrefactor.structure.brige;

public class PayByCypto implements PayMode{
    @Override
    public void payMethod(String uid) {
        System.out.println("密码支付");
    }
}
