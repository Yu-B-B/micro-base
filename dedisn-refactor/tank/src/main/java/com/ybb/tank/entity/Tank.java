package com.ybb.tank.entity;

import com.ybb.tank.content.Direction;
import com.ybb.tank.TankFrame;
import com.ybb.tank.content.Group;
import lombok.Data;

import java.awt.*;
import java.util.Random;

import static com.ybb.tank.content.ContentData.*;
import static com.ybb.tank.content.StaticResource.*;

@Data
public class Tank {
    private int x, y;
    private Direction direction = Direction.UP; // 朝向
    // 控制所有坦克移动标识，true自动移动，false不动
    private boolean moving = true;
    private TankFrame tf = null; // 上层属性
    private boolean live = true; // 是否存活
    private Group group = Group.BAD; // 区分敌我
    //
    private Random random = new Random();

    public Tank(int x, int y, Direction direction, TankFrame tankFrame, Group group) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tf = tankFrame;
        this.group = group;
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
        if(group.equals(Group.BAD)){
            switch (direction) {
                case LEFT:
                    g.drawImage(tankL, x, y, null);
                    break;
                case RIGHT:
                    g.drawImage(tankR, x, y, null);
                    break;
                case UP:
                    g.drawImage(tankU, x, y, null);
                    break;
                case DOWN:
                    g.drawImage(tankD, x, y, null);
                    break;
            }
        }
        if(group.equals(Group.GOOD)){
            switch (direction) {
                case LEFT:
                    g.drawImage(myTankL, x, y, null);
                    break;
                case RIGHT:
                    g.drawImage(myTankR, x, y, null);
                    break;
                case UP:
                    g.drawImage(myTankU, x, y, null);
                    break;
                case DOWN:
                    g.drawImage(myTankD, x, y, null);
                    break;
            }
        }

        move();
    }

    private void move() {
        if (!moving) return;
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
        if(group.equals(Group.BAD)){
            // 敌方坦克打出子弹
            if (random.nextInt(10) > 8) {
                this.fire();
            }
            if(random.nextInt(100)<2){
                this.direction = Direction.values()[random.nextInt(Direction.values().length)];
            }
            checkBorder();
        }

    }

    /**
     * 边界检查
     * 触碰到边界后随机方向移动
     */
    private void checkBorder(){
        boolean touch = false;
        if(x < 0) { x = 0; touch = true; }
        if(y < 0) { y = 0; touch = true; }
        if(x > SCREEN_WIDTH - TANK_WIDTH) { x = SCREEN_WIDTH - TANK_WIDTH; touch = true; }
        if(y > SCREEN_HEIGHT - TANK_HEIGHT) { y = SCREEN_HEIGHT - TANK_HEIGHT; touch = true; }
        if(touch) {
            Direction[] directions = Direction.values();
            Direction oldDir = this.direction;
            // 随机新方向，不能和原方向一样
            Direction newDir;
            do {
                newDir = directions[random.nextInt(directions.length)];
            } while(newDir == oldDir);
            this.direction = newDir;
        }
    }

    public void fire() {
        int bx = x + TANK_WIDTH / 2 - TANK_BULLET_WIDTH / 2;
        int by = y + TANK_HEIGHT / 2 - TANK_BULLET_HEIGHT / 2;
        tf.bullets.add(new Bullet(bx, by, direction, this.tf, this.group));
    }

    public void destory() {
        this.live = false;
    }
}
