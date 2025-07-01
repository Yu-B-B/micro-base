package com.ybb.tank.entity;

import com.ybb.tank.Direction;
import com.ybb.tank.TankFrame;
import lombok.Data;

import java.awt.*;

import static com.ybb.tank.content.ContentData.MY_TANK_SPEED;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Tank {
    private int x, y;
    private Direction direction = Direction.UP;
    private boolean moving = false;
    private TankFrame tf = null;

    public Tank(int x, int y, Direction direction, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tf = tankFrame;
    }

    /**
     * 根据方向设置坦克使用图片
     * @param g
     */
    public void paint(Graphics g) {
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
    }

    public void fire() {
        System.out.println("main tank x - y "+x+" "+y);
        tf.bullets.add(new Bullet(x, y, direction, this.tf));
    }
}
