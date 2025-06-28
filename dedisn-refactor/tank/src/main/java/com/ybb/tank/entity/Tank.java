package com.ybb.tank.entity;

import com.ybb.tank.Direction;
import lombok.Data;

import java.awt.*;
import static com.ybb.tank.content.ContentData.MY_TANK_SPEED;
import static com.ybb.tank.content.ContentData.MY_TANK_SIZE;

@Data
public class Tank {
    private int x,y;
    private Direction direction = Direction.UP;
    private boolean moving = false;

    public Tank(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void paint(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.BLUE);
        g.fillRect(x, y, MY_TANK_SIZE,MY_TANK_SIZE);
        g.setColor(color);
        move();
    }

    private void move() {
        if(!moving) return;
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
}
