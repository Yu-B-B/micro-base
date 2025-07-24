package com.ybb.tank.entity;

import com.ybb.tank.config.PropertiesUtils;
import com.ybb.tank.content.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.Group;
import com.ybb.tank.strategy.DefaultFireStrategyImpl;
import com.ybb.tank.strategy.FireStrategy;
import com.ybb.tank.strategy.FourDirectionFireStrategyImpl;
import lombok.Data;

import java.awt.*;
import java.util.Random;

import static com.ybb.tank.content.ContentData.*;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Tank {
    public int x, y;
    public Direction direction = Direction.UP; // 朝向
    // 控制所有坦克移动标识，true自动移动，false不动
    private boolean moving = true;
    public TankFrame tf = null; // 上层属性
    private boolean live = true; // 是否存活
    public Group group = Group.BAD; // 区分敌我
    private Random random = new Random();
    public int WIDTH = TANK_WIDTH;
    public int HEIGHT = TANK_HEIGHT;

    public Rectangle tankRect = new Rectangle();

    FireStrategy fireStrategy = new FourDirectionFireStrategyImpl();

    public Tank(int x, int y, Direction direction, TankFrame tankFrame, Group group) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tf = tankFrame;
        this.group = group;

        tankRect.x = x;
        tankRect.y = y;
        tankRect.width = WIDTH;
        tankRect.height = HEIGHT;
    }

    /**
     * 根据方向设置坦克使用图片
     *
     * @param g
     */
    public void paint(Graphics g) {
        if (!live) {
            tf.enemyTanks.remove(this);
        }
        switch (direction) {
            case LEFT:
                g.drawImage(group.equals(Group.BAD) ? tankL : myTankL, x, y, null);
                break;
            case RIGHT:
                g.drawImage(group.equals(Group.BAD) ? tankR : myTankR, x, y, null);
                break;
            case UP:
                g.drawImage(group.equals(Group.BAD) ? tankU : myTankU, x, y, null);
                break;
            case DOWN:
                g.drawImage(group.equals(Group.BAD) ? tankD : myTankD, x, y, null);
                break;
        }

        move();
    }

    private void move() {
        if (!moving) return;
        int speed = PropertiesUtils.MY_TANK_SPEED;
        switch (direction) {
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            default:
                break;
        }
        tankRect.x = x;
        tankRect.y = y;
        if (group.equals(Group.BAD)) {
            // 敌方坦克打出子弹
            if (random.nextInt(10) > 8) {
                this.fire();
            }
            if (random.nextInt(100) < 2) {
                this.direction = Direction.values()[random.nextInt(Direction.values().length)];
            }
            checkBorder();
        }
    }

    /**
     * 边界检查
     * 触碰到边界后随机方向移动
     */
    private void checkBorder() {
        boolean touch = false;
        if(x < 0) { x = 0; touch = true; }
        // y轴要减去上面的状态栏
        if(y < 30) { y = 30; touch = true; }
        if(x > PropertiesUtils.SCREEN_WIDTH - WIDTH) { x = PropertiesUtils.SCREEN_WIDTH - WIDTH; touch = true; }
        if(y > PropertiesUtils.SCREEN_HEIGHT - HEIGHT) { y = PropertiesUtils.SCREEN_HEIGHT - HEIGHT; touch = true; }
        if(touch) {
            Direction[] directions = Direction.values();
            Direction oldDir = this.direction;
            // 随机新方向，不能和原方向一样
            Direction newDir;
            do {
                newDir = directions[random.nextInt(directions.length)];
            } while (newDir == oldDir);
            this.direction = newDir;
        }
    }

    public void fire() {
        // fire使用策略模式调用，当策略类作为参数传递时，需要写为单例模式
        // 或者将使用的策略模式写在当前的类中
        fireStrategy.fire(this);
    }

    public void destory() {
        this.live = false;
    }
}
