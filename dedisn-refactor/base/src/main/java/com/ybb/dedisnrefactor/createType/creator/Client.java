package com.ybb.dedisnrefactor.createType.creator;

import com.ybb.dedisnrefactor.createType.creator.demo.MyBikeBuilder;
import com.ybb.dedisnrefactor.createType.creator.director.Director;
import com.ybb.dedisnrefactor.createType.creator.product.Bike;

/**
 * 客户端
 * @author spikeCong
 * @date 2022/9/19
 **/
public class Client {

    public static void main(String[] args) {

        //1.创建指挥者
//        Director director = new Director(new MobikeBuilder());
        Director director = new Director(new MyBikeBuilder("黄金","真皮"));

        //2.获取自行车
        Bike bike = director.construct();
        System.out.println(bike.getFrame() + "," + bike.getSeat());
    }

}
