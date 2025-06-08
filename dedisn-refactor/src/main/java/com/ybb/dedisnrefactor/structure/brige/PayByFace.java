package com.ybb.dedisnrefactor.structure.brige;

public class PayByFace implements PayMode{
    @Override
    public void payMethod(String uid) {
        System.out.println("人脸支付");
    }
}
