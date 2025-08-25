package com.ybb.abstractfactory;

import com.ybb.tank.TankFrame;
import com.ybb.tank.content.StaticResource;

import java.awt.*;

public class RectExposed extends BaseExplode{
    public static int WIDTH = StaticResource.expose[0].getWidth();
    public static int HEIGHT = StaticResource.expose[0].getHeight();
    private int x, y;
    private boolean live = true;
    private int step = 0;
    private TankFrame tf = null;


    public RectExposed(int x, int y,  TankFrame tf) {
        this.x = x;
        this.y = y;
        this.tf = tf;
    }

    /**
     * 绘制爆炸,
     * @param grp
     */
    @Override
    public void paint(Graphics grp) {
        grp.drawImage(StaticResource.expose[step++],x,y,null);
        if(step>=StaticResource.expose.length) {
            step = 0;
            tf.exposes.remove(this);
        }
    }
}
