package com.ybb.tank;

import com.ybb.tank.content.ContentData;
import com.ybb.tank.content.Direction;
import com.ybb.tank.content.Group;
import com.ybb.tank.entity.Bullet;
import com.ybb.tank.entity.Expose;
import com.ybb.tank.entity.Tank;
import com.ybb.tank.listener.MainTankListener;
import lombok.extern.slf4j.Slf4j;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ybb.tank.content.ContentData.SCREEN_HEIGHT;
import static com.ybb.tank.content.ContentData.SCREEN_WIDTH;
import static com.ybb.tank.content.ContentData.MY_TANK_DEFAULT_X;
import static com.ybb.tank.content.ContentData.MY_TANK_DEFAULT_Y;

@Slf4j
public class TankFrame extends Frame {

    Tank mainTank = new Tank(MY_TANK_DEFAULT_X, MY_TANK_DEFAULT_Y, Direction.UP, this, Group.GOOD);
    public List<Tank> enemyTanks = new ArrayList<>(10);
    public List<Bullet> bullets = new ArrayList<>();
    public List<Expose> exposes = new ArrayList<>();

    public TankFrame() {
        setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setTitle("tank war");
        setResizable(false);

        // 监听键盘按钮，控制我方坦克移动
        addKeyListener(new MainTankListener(mainTank));

        // 关闭窗口
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void init() {
        for (int i = 0; i < 10; i++) {
            int width = ContentData.SCREEN_WIDTH - ContentData.MY_TANK_SIZE;
            int height = ContentData.SCREEN_HEIGHT - ContentData.MY_TANK_SIZE;
            Random random = new Random();

            int iw = random.nextInt(width);
            int ih = random.nextInt(height);

            // 随机方向
            Direction[] directions = Direction.values();
            Direction dir = directions[random.nextInt(directions.length)];

            enemyTanks.add(new Tank(20 + i * 90, 200, dir, this, Group.BAD));
        }
    }

    @Override
    public void paint(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString("子弹数量" + bullets.size(), 10, 60);
        g.setColor(color);

        // 绘制我方坦克
        mainTank.paint(g);
        // 绘制敌方坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            enemyTanks.get(i).paint(g);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }

        // 碰撞检测，移除敌方坦克
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < enemyTanks.size(); j++) {
                bullets.get(i).intersect(enemyTanks.get(j));
            }
        }
        // 绘制爆炸
        for (int i = 0; i < exposes.size(); i++) {
            exposes.get(i).paint(g);
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
        graphics.setColor(Color.BLACK); // 只是对当前对象重新创建颜色，设置后将颜色回滚到之前的样式
        graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        graphics.setColor(color);
        paint(graphics);
        g.drawImage(offectScreenImage, 0, 0, null);
    }


}
