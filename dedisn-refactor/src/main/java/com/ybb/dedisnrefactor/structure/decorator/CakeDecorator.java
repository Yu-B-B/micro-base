package com.ybb.dedisnrefactor.structure.decorator;

public class CakeDecorator implements Cake{
    protected Cake cake;

    public CakeDecorator(Cake cake) {
        this.cake = cake;
    }


    @Override
    public String getName() {
        return cake.getName();
    }

    @Override
    public Integer getPrice() {
        return cake.getPrice();
    }
}
