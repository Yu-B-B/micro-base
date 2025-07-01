package com.ybb.tank.entity;

import com.ybb.tank.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.ContentData;
import com.ybb.tank.content.StaticResource;
import lombok.Data;

import java.awt.*;

import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SIZE;
import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SPEED;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Bullet {
    private int x, y;
    private Direction direction;

    private boolean live = true;

    private TankFrame tf = null;
    // 特殊属性


    public Bullet(int x, int y, Direction dir, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.direction = dir;
        this.tf = tf;
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
//        checkBoom();
    }

    // 子弹与地方坦克碰撞
    private void checkBoom() {
        System.out.println("main bullet x - y" + x + " " + y);
        if (!tf.elemTank.isEmpty()) {
            boolean flag = false;
            for (int i = 0; i < tf.elemTank.size(); i++) {
                Tank elTank = tf.elemTank.get(i);
                int bulletX = x + MY_TANK_BULLET_SIZE;
                int bulletY = y + MY_TANK_BULLET_SIZE;
                System.out.println("ele tank x - y" + bulletX + " " + bulletY);
                // 正向碰撞和反向碰撞
                if (direction.equals(Direction.UP)) { // bullet 向上，x不变，y减小
                    int tankY = elTank.getY() + ContentData.MY_TANK_SIZE;
                    if (bulletY < tankY) {
                        System.err.println("发生碰撞");
                        break;
                    }
                }

            }
            if (!flag) {
                // 移除当前坦克和子弹
                tf.bullets.remove(this);
            }
        }
    }
}
