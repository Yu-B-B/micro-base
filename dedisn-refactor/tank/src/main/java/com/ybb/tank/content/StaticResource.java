package com.ybb.tank.content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * 静态资源管理
 */
public class StaticResource {
    public static BufferedImage tankL, tankU, tankR, tankD;
    public static BufferedImage myTankL, myTankU, myTankR, myTankD;
    public static BufferedImage bulletL, bulletU, bulletR, bulletD;
    public static BufferedImage[] expose = new BufferedImage[16];

    // 加载静态资源，方便后续使用
    static {
        try {
            tankU = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/BadTank1.png"));
            tankL = rotateImage(tankU, -90);
            tankR = rotateImage(tankU, 90);
            tankD = rotateImage(tankU, 180);

            myTankU = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/GoodTank1.png"));
            myTankL = rotateImage(myTankU, -90);
            myTankR = rotateImage(myTankU, 90);
            myTankD = rotateImage(myTankU, 180);

            bulletU = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/bulletU.png"));
            bulletL = rotateImage(bulletU, -90);
            bulletR = rotateImage(bulletU, 90);
            bulletD = rotateImage(bulletU, 180);

            for (int i = 0; i < 16; i++) {
                expose[i] = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/e"+(i+1)+".gif"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage rotateImage(BufferedImage src, double angle) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage result = new BufferedImage(w, h, src.getType());
        Graphics2D g2 = result.createGraphics();
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), w/2.0, h/2.0);
        g2.drawImage(src, at, null);
        g2.dispose();
        return result;
    }
}
