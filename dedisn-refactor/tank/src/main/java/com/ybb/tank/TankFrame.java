package com.ybb.tank;

import com.ybb.tank.entity.Bullet;
import com.ybb.tank.entity.Tank;
import lombok.extern.slf4j.Slf4j;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static com.ybb.tank.content.ContentData.SCREEN_HEIGHT;
import static com.ybb.tank.content.ContentData.SCREEN_WIDTH;
import static com.ybb.tank.content.ContentData.MY_TANK_DEFAULT_X;
import static com.ybb.tank.content.ContentData.MY_TANK_DEFAULT_Y;

@Slf4j
public class TankFrame extends Frame {

    Tank mainTank = new Tank(MY_TANK_DEFAULT_X, MY_TANK_DEFAULT_Y, Direction.UP,this);
    public List<Tank> elemTank = new ArrayList<>(10);
    public List<Bullet> bullets = new ArrayList<>();
//    public Bullet bullet = new Bullet(MY_TANK_BULLET_X, MY_TANK_BULLET_Y, Direction.UP);

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
        Color color = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString("子弹数量"+bullets.size(),10,60);
        g.setColor(color);

        mainTank.paint(g); // 主坦
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }
//        bullet.paint(g); // 主坦克子弹
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
                case KeyEvent.VK_SPACE:
                    mainTank.fire();
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
                mainTank.setMoving(false);
            } else {
                mainTank.setMoving(true);
                if (BU) mainTank.setDirection(Direction.UP);
                if (BL) mainTank.setDirection(Direction.LEFT);
                if (BR) mainTank.setDirection(Direction.RIGHT);
                if (BD) mainTank.setDirection(Direction.DOWN);

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
        graphics.setColor(Color.WHITE); // 只是对当前对象重新创建颜色，设置后将颜色回滚到之前的样式
        graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        graphics.setColor(color);
        paint(graphics);
        g.drawImage(offectScreenImage, 0, 0, null);
    }
}
