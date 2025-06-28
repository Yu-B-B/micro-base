package com.ybb.tank;

import com.ybb.tank.entity.Bullet;
import com.ybb.tank.entity.Tank;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.ybb.tank.content.ContentData.SCREEN_HEIGHT;
import static com.ybb.tank.content.ContentData.SCREEN_WIDTH;
import static com.ybb.tank.content.ContentData.MY_TANK_DEFAULT_X;
import static com.ybb.tank.content.ContentData.MY_TANK_DEFAULT_Y;
import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_X;
import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_Y;

@Slf4j
public class TankFrame extends Frame {

    Tank tank = new Tank(MY_TANK_DEFAULT_X, MY_TANK_DEFAULT_Y);
    Bullet bullet = new Bullet(MY_TANK_BULLET_X, MY_TANK_BULLET_Y, Direction.UP);

    private static boolean BL = false;
    private static boolean BU = false;
    private static boolean BR = false;
    private static boolean BD = false;


    public TankFrame() {
        setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
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
        tank.paint(g); // 主坦克
        bullet.paint(g); // 主坦克子弹
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

    // 双缓冲解决闪烁问题。先将需要绘制的内容放到缓冲中，在缓冲中绘制完成后一起绘制到画面
    Image offectScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offectScreenImage == null) {
            offectScreenImage = this.createImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        Graphics graphics = offectScreenImage.getGraphics();
        Color color = graphics.getColor();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        graphics.setColor(color);
        paint(graphics);
        g.drawImage(offectScreenImage, 0, 0, null);
    }
}
