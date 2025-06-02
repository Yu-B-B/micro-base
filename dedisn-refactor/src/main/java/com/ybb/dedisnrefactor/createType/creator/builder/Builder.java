package com.ybb.dedisnrefactor.createType.creator.builder;

import com.ybb.dedisnrefactor.createType.creator.product.Bike;

public abstract class Builder {
    protected Bike mBike = new Bike();

    public abstract void buildFrame();
    public abstract void buildSeat();
    public abstract Bike createBike();
}
