package com.ybb.abstractfactory;

import com.ybb.tank.TankFrame;
import com.ybb.tank.content.Direction;
import com.ybb.tank.content.Group;
import com.ybb.tank.entity.Expose;
import com.ybb.tank.entity.Tank;

public class DefaultFactory extends GameFactory{
    @Override
    public BaseTank createTank(int x, int y, Direction direction, Group group, TankFrame frame) {
        return new Tank(x,y,direction,frame,group);
    }

    @Override
    public BaseExplode createExplode(int x, int y, TankFrame tankFrame) {
        return new Expose(x,y,tankFrame);
    }

    @Override
    public BaseBullet createBullet(int x, int y, TankFrame tankFrame) {
        return null;
    }
}
