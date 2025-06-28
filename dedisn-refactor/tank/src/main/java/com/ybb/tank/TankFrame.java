package main.java.com.ybb.tank;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankFrame extends Frame {

    public TankFrame() {
        setVisible(true);
        setSize(800, 600);
        setTitle("tank war");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private int x = 100;
    private int y = 100;
    @Override
    public void paint(Graphics g) {
        System.out.println("PAIN");
        g.fillRect(x,y,50,50);
        x += 10;
        y += 10;

    }
}
