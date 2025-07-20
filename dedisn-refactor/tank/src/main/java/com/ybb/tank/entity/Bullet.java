package com.ybb.tank.entity;

import com.ybb.tank.content.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.ContentData;
import com.ybb.tank.content.Group;
import lombok.Data;

import java.awt.*;

import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SPEED;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Bullet {
    private int x, y;
    private Direction direction;

    private boolean live = true;

    private TankFrame tf = null;
    private Group group = Group.BAD;


    public Bullet(int x, int y, Direction dir, TankFrame tf, Group group) {
        this.x = x;
        this.y = y;
        this.direction = dir;
        this.tf = tf;
        this.group = group;
    }

    /**
     * 绘制子弹,
     * 子弹射出位置应该为坦克方向中心
     * @param grp
     */
    public void paint(Graphics grp) {
        if (!live) {
            tf.bullets.remove(this);
        }
        switch (direction) {
            case LEFT:
                grp.drawImage(bulletL, x, y, null);
                break;
            case RIGHT:
                grp.drawImage(bulletR, x, y, null);
                break;
            case UP:
                grp.drawImage(bulletU, x, y, null);
                break;
            case DOWN:
                grp.drawImage(bulletD, x, y, null);
                break;
        }
        move();
    }

    private void move() {
        switch (direction) {
            case LEFT:
                x -= MY_TANK_BULLET_SPEED;
                break;
            case RIGHT:
                x += MY_TANK_BULLET_SPEED;
                break;
            case UP:
                y -= MY_TANK_BULLET_SPEED;
                break;
            case DOWN:
                y += MY_TANK_BULLET_SPEED;
                break;
            default:
                break;
        }
        if (x < 0 || y < 0 || x > ContentData.SCREEN_WIDTH || y > ContentData.SCREEN_HEIGHT) live = false;
    }

    public void destory(){
        this.live = false;
    }

    /**
     * 发生碰撞时
     * @param tank
     * @return
     */
    public void intersect(Tank tank) {
        if(this.group == tank.getGroup()) return;
        Rectangle bulletArea = new Rectangle(this.x, this.y, bulletD.getWidth(), bulletD.getHeight());
        Rectangle tankArea = new Rectangle(tank.getX(), tank.getY(), tankD.getWidth(), tankD.getHeight());
        if(bulletArea.intersects(tankArea)) {
            this.destory();
            tank.destory();
        }
    }
}
