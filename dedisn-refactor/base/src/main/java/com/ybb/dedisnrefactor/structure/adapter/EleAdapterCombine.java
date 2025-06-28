package com.ybb.dedisnrefactor.structure.adapter;

public class EleAdapterCombine implements AmericanEle{

    private ChinaEle chinaEle;

    public EleAdapterCombine(ChinaEle chinaEle) {
        this.chinaEle = chinaEle;
    }

    @Override
    public void currentUse() {
        System.out.println("adapter start change ele");
        chinaEle.needUse();
    }
}
