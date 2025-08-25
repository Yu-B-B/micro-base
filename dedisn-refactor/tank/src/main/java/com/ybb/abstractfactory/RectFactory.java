package com.ybb.abstractfactory;

import com.ybb.tank.TankFrame;
import com.ybb.tank.content.Direction;
import com.ybb.tank.content.Group;

public class RectFactory extends GameFactory{
    @Override
    public BaseTank createTank(int x, int y, Direction direction, Group group, TankFrame frame) {
        return null;
    }

    @Override
    public BaseExplode createExplode(int x, int y, TankFrame tankFrame) {
        return new RectExposed(x,y,tankFrame);
    }

    @Override
    public BaseBullet createBullet(int x, int y, TankFrame tankFrame) {
        return null;
    }
}
