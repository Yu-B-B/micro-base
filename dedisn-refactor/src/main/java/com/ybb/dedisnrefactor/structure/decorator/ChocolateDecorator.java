package com.ybb.dedisnrefactor.structure.decorator;

public class ChocolateDecorator extends CakeDecorator{
    public ChocolateDecorator(Cake cake) {
        super(cake);
    }

    public String getDes(){
        return cake.getName() + " "+"加巧克力";
    }

    public Integer getPrice(){
        return cake.getPrice() + 10;
    }
}
