package com.ybb.tank.entity;

import com.ybb.tank.config.PropertiesUtils;
import com.ybb.tank.content.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.ContentData;
import com.ybb.tank.content.Group;
import lombok.Data;

import java.awt.*;

import static com.ybb.tank.content.ContentData.*;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Bullet {
    private static int WIDTH = bulletD.getWidth();
    private static int HEIGHT = bulletD.getHeight();
    private int x, y;
    private Direction direction;
    // 是否绘制
    private boolean live = true;

    private TankFrame tf = null;
    private Group group = Group.BAD;
    // 子弹空间
    Rectangle rect = new Rectangle();

    public Bullet(int x, int y, Direction dir, TankFrame tf, Group group) {
        this.x = x;
        this.y = y;
        this.direction = dir;
        this.tf = tf;
        this.group = group;

        rect.x = x;
        rect.y = y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
        tf.bullets.add(this);
    }

    /**
     * 绘制子弹,
     * 子弹射出位置应该为坦克方向中心
     *
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
        int bulletSpeed = PropertiesUtils.BULLET_SPEED;
        switch (direction) {
            case LEFT:
                x -= bulletSpeed;
                break;
            case RIGHT:
                x += bulletSpeed;
                break;
            case UP:
                y -= bulletSpeed;
                break;
            case DOWN:
                y += bulletSpeed;
                break;
            default:
                break;
        }
        rect.x = x;
        rect.y = y;
        if (x < 0 || y < 0 || x > PropertiesUtils.SCREEN_WIDTH || y > PropertiesUtils.SCREEN_HEIGHT) live = false;
    }

    public void destory() {
        this.live = false;
    }

    /**
     * 子弹与坦克碰撞时
     *
     * @param tank
     * @return
     */
    public void intersect(Tank tank) {
        // 如果自己坦克与自己子弹碰撞，返回
        if (this.group == tank.getGroup()) return;
        // 双方正营子弹与坦克碰撞
        if (rect.intersects(tank.tankRect)) {
            this.destory(); // 子弹移除
            tank.destory(); // 坦克移除
            // 爆炸
            int ex = tank.getX() + TANK_WIDTH / 2 - Expose.WIDTH / 2;
            int ey = tank.getY() + TANK_HEIGHT / 2 - Expose.HEIGHT / 2;
            tf.exposes.add(new Expose(ex, ey, tf));
        }
    }
}
