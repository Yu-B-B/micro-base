package com.ybb.dedisnrefactor.structure.adapter;

public class Client {
    public static void main(String[] args) {

        Phone phone = new Phone();

        AmericanEleImpl americanEle = new AmericanEleImpl();
        phone.recd(americanEle);

        System.out.print("---");

        // 方式一：勾走方法中没有参数，看起来简单一些，但继承了 ChiaEle，在
//        EleAdapter adapter = new EleAdapter();

        EleAdapterCombine adapter = new EleAdapterCombine(new ChinaEle());

        phone.recd(adapter);
    }
}
