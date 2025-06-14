package com.ybb.dedisnrefactor.structure.adapter;


/**
 * 将美国的插头在中国使用
 */
public class EleAdapter extends ChinaEle implements AmericanEle{

    @Override
    public void currentUse() {
        System.out.println("开始使用中国电流充电");
        needUse();
    }
}
