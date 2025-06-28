package com.ybb.tank;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
public class TankFrame extends Frame {

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


    private int x = 100;
    private int y = 100;
    @Override
    public void paint(Graphics g) {
        System.out.println("PAIN");
        g.fillRect(x,y,50,50);
        x += 10;
        y += 10;

    }

    private class MyKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) { // 键盘按下
            log.info("当前按下的按钮{}",e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) { // 键盘抬起
            log.info("当前抬起的按钮{}",e.getKeyCode());

        }
    }
}
