package com.ybb.dedisnrefactor.structure.decorator;

public class BaseCake implements Cake{

    @Override
    public String getName() {
        return "原味";
    }

    @Override
    public Integer getPrice() {
        return 10;
    }
}
