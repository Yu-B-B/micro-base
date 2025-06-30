package com.ybb.tank;

import com.ybb.tank.content.ContentData;

import java.util.Random;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        TankFrame frame = new TankFrame();
        frame.init();
        while (true) {
            Thread.sleep(50);

            frame.repaint();
        }
    }
}
