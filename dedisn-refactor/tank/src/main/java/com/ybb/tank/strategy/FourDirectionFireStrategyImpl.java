package com.ybb.tank.strategy;

import com.ybb.tank.content.Direction;
import com.ybb.tank.entity.Bullet;
import com.ybb.tank.entity.Tank;

import static com.ybb.tank.content.ContentData.TANK_BULLET_HEIGHT;
import static com.ybb.tank.content.ContentData.TANK_BULLET_WIDTH;

public class FourDirectionFireStrategyImpl implements FireStrategy{
    @Override
    public void fire(Tank t) {
        int bx = t.x + t.WIDTH / 2 - TANK_BULLET_WIDTH / 2;
        int by = t.y + t.HEIGHT / 2 - TANK_BULLET_HEIGHT / 2;
        new Bullet(bx, by, Direction.UP, t.tf, t.group);
        new Bullet(bx, by, Direction.DOWN, t.tf, t.group);
        new Bullet(bx, by, Direction.LEFT, t.tf, t.group);
        new Bullet(bx, by, Direction.RIGHT, t.tf, t.group);
    }
}
