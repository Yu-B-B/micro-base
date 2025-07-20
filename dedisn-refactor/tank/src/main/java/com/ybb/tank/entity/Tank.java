package com.ybb.tank.entity;

import com.ybb.tank.content.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.Group;
import lombok.Data;

import java.awt.*;
import java.util.Random;

import static com.ybb.tank.content.ContentData.*;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Tank {
    private int x, y;
    private Direction direction = Direction.UP; // 朝向
    private boolean moving = true; // 是否自动移动
    private TankFrame tf = null; // 上层属性
    private boolean live = true; // 是否存活
    private Group group = Group.BAD; // 区分敌我
    private Random random = new Random();


    public Tank(int x, int y, Direction direction, TankFrame tankFrame, Group group) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tf = tankFrame;
        this.group = group;
    }

    /**
     * 根据方向设置坦克使用图片
     *
     * @param g
     */
    public void paint(Graphics g) {
        if (!live) {
            tf.elemTank.remove(this);
        }
        ;
        switch (direction) {
            case LEFT:
                g.drawImage(tankL, x, y, null);
                break;
            case RIGHT:
                g.drawImage(tankR, x, y, null);
                break;
            case UP:
                g.drawImage(tankU, x, y, null);
                break;
            case DOWN:
                g.drawImage(tankD, x, y, null);
                break;
        }
        move();
    }

    private void move() {
        if (!moving) return;
        switch (direction) {
            case LEFT:
                x -= MY_TANK_SPEED;
                break;
            case RIGHT:
                x += MY_TANK_SPEED;
                break;
            case UP:
                y -= MY_TANK_SPEED;
                break;
            case DOWN:
                y += MY_TANK_SPEED;
                break;
            default:
                break;
        }
        if (random.nextInt(10) > 8) this.fire();
    }

    public void fire() {
        int bx = x + TANK_WIDTH / 2 - TANK_BULLET_WIDTH / 2;
        int by = y + TANK_HEIGHT / 2 - TANK_BULLET_HEIGHT / 2;
        tf.bullets.add(new Bullet(bx, by, direction, this.tf, this.group));
    }

    public void destory() {
        this.live = false;
    }
}
