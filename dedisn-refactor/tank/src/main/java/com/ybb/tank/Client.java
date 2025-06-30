package com.ybb.tank;

import com.ybb.tank.content.ContentData;

import java.util.Random;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        TankFrame frame = new TankFrame();
        while (true) {
            Thread.sleep(50);

            int width = ContentData.SCREEN_WIDTH;
            int height = ContentData.SCREEN_HEIGHT;
            Random random = new Random();

            System.out.println("随机宽度: " + random.nextInt(width));
            System.out.println("随机高度: " + random.nextInt(height));

            frame.repaint();
        }
    }
}
