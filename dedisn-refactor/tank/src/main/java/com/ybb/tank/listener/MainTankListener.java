package com.ybb.tank.listener;

import com.ybb.tank.entity.Tank;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.ybb.tank.content.Direction.UP;
import static com.ybb.tank.content.Direction.LEFT;
import static com.ybb.tank.content.Direction.RIGHT;
import static com.ybb.tank.content.Direction.DOWN;

public class MainTankListener extends KeyAdapter {
    private static boolean BL = false;
    private static boolean BU = false;
    private static boolean BR = false;
    private static boolean BD = false;

    private Tank mainTank = null;
    public MainTankListener(Tank mainTank){
        this.mainTank = mainTank;
    }

    @Override
    public void keyPressed(KeyEvent e) { // 键盘按下
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W:
                BU = true;
                break;
            case KeyEvent.VK_S:
                BD = true;
                break;
            case KeyEvent.VK_A:
                BL = true;
                break;
            case KeyEvent.VK_D:
                BR = true;
                break;
            case KeyEvent.VK_SPACE:
                mainTank.fire();
                break;
            default:
                break;
        }

        setTankMainDir();
    }

    @Override
    public void keyReleased(KeyEvent e) { // 键盘抬起
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W:
                BU = false;
                break;
            case KeyEvent.VK_S:
                BD = false;
                break;
            case KeyEvent.VK_A:
                BL = false;
                break;
            case KeyEvent.VK_D:
                BR = false;
                break;
            default:
                break;
        }
        setTankMainDir();
    }

    private void setTankMainDir() {
        if (!BL && !BU && !BR && !BD) {
            mainTank.setMoving(false);
        } else {
            mainTank.setMoving(true);
            if (BU) mainTank.setDirection(UP);
            if (BL) mainTank.setDirection(LEFT);
            if (BR) mainTank.setDirection(RIGHT);
            if (BD) mainTank.setDirection(DOWN);

        }
    }
}