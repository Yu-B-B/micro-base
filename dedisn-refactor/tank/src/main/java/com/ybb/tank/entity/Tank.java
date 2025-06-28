package com.ybb.tank.entity;

import com.ybb.tank.Direction;
import lombok.Data;

import java.awt.*;

@Data
public class Tank {
    private int x,y;
    private Direction direction = Direction.UP;
    private static final int SPEED = 20;
    private boolean moving = false;

    public Tank(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void paint(Graphics g) {
        g.fillRect(x, y, SPEED,SPEED);
        move();
    }

    private void move() {
        if(!moving) return;
        switch (direction) {
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            default:
                break;
        }
    }
}
