package com.ybb.tank;

import com.ybb.tank.entity.Tank;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
public class TankFrame extends Frame {

    Tank tank = new Tank(390, 580);

    private static boolean BL = false;
    private static boolean BU = false;
    private static boolean BR = false;
    private static boolean BD = false;


    public TankFrame() {
        setVisible(true);
        setSize(800, 600);
        setTitle("tank war");
        setResizable(false);


        // 键盘按钮监听，让页面动态刷新
        addKeyListener(new MyKeyListener());

        // 关闭窗口监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        tank.paint(g);

    }

    private class MyKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) { // 键盘按下
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_W:
                    BU = true;
                    break;
                case KeyEvent.VK_S:
                    BD = true;
                    break;
                case KeyEvent.VK_A:
                    BL = true;
                    break;
                case KeyEvent.VK_D:
                    BR = true;
                    break;
                default:
                    break;
            }

            setTankMainDir();
        }

        @Override
        public void keyReleased(KeyEvent e) { // 键盘抬起
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_W:
                    BU = false;
                    break;
                case KeyEvent.VK_S:
                    BD = false;
                    break;
                case KeyEvent.VK_A:
                    BL = false;
                    break;
                case KeyEvent.VK_D:
                    BR = false;
                    break;
                default:
                    break;
            }
            setTankMainDir();
        }

        private void setTankMainDir() {
            if (!BL && !BU && !BR && !BD) {
                tank.setMoving(false);
            } else {
                tank.setMoving(true);
                if (BU) tank.setDirection(Direction.UP);
                if (BL) tank.setDirection(Direction.LEFT);
                if (BR) tank.setDirection(Direction.RIGHT);
                if (BD) tank.setDirection(Direction.DOWN);

            }
        }
    }
}
