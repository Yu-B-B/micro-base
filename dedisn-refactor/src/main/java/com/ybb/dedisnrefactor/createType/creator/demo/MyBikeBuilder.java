package com.ybb.dedisnrefactor.createType.creator.demo;

import com.ybb.dedisnrefactor.createType.creator.builder.Builder;
import com.ybb.dedisnrefactor.createType.creator.product.Bike;

public class MyBikeBuilder extends Builder {
    private String frame;
    private String seat;

    public MyBikeBuilder(String frame, String seat) {
        this.frame = frame;
        this.seat = seat;
    }

    @Override
    public void buildFrame() {
        mBike.setFrame(frame);
    }

    @Override
    public void buildSeat() {
        mBike.setSeat(seat);
    }

    @Override
    public Bike createBike() {
        return mBike;
    }
}
