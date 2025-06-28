package com.ybb.tank;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        TankFrame frame = new TankFrame();
        while (true) {
            Thread.sleep(40);
            frame.repaint();
        }
    }
}
