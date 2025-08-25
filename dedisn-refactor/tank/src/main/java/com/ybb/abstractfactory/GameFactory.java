package com.ybb.abstractfactory;

import com.ybb.tank.TankFrame;
import com.ybb.tank.content.Direction;
import com.ybb.tank.content.Group;

public abstract class GameFactory {
    public abstract BaseTank createTank(int x, int y, Direction direction, Group group,TankFrame frame);
    public abstract BaseExplode createExplode(int x, int y, TankFrame tankFrame);
    public abstract BaseBullet createBullet(int x,int y,TankFrame tankFrame);
}
