package com.ybb.tank.entity;

import com.ybb.tank.Direction;
import lombok.Data;

import java.awt.*;
import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SIZE;
import static com.ybb.tank.content.ContentData.MY_TANK_BULLET_SPEED;

@Data
public class Bullet {
    private int x, y;
    private Direction direction;


    public Bullet(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.direction = dir;
    }

    public void paint(Graphics grp){
        Color color = grp.getColor();
        grp.setColor(Color.BLACK);
        grp.fillRect(x,y,MY_TANK_BULLET_SIZE,MY_TANK_BULLET_SIZE);
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
    }
}
