package com.ybb.tank.entity;

import com.ybb.tank.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.ContentData;
import lombok.Data;

import java.awt.*;

import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SIZE;
import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SPEED;

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
        this.live = true;
    }

    public void paint(Graphics grp) {
        if(!live)
        {
            tf.bullets.remove(this);
        }
        Color color = grp.getColor();
        grp.setColor(Color.BLACK);
        grp.fillRect(x, y, MY_TANK_BULLET_SIZE, MY_TANK_BULLET_SIZE);
        grp.setColor(color);
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
}
