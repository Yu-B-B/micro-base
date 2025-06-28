package com.ybb.dedisnrefactor.structure.brige;

public abstract class Pay {
    protected PayMode payMode;

    public Pay(PayMode payMode) {
        this.payMode = payMode;
    }

    public abstract void transfer(String uid);

}
