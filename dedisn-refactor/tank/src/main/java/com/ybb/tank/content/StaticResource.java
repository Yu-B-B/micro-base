package com.ybb.tank.content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 静态资源管理
 */
public class StaticResource {
    public static BufferedImage tankL, tankU, tankR, tankD;
    public static BufferedImage bulletL, bulletU, bulletR, bulletD;

    // 加载静态资源，方便后续使用
    static {
        try {
            tankL = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankL.gif"));
            tankU = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankU.gif"));
            tankR = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankR.gif"));
            tankD = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankD.gif"));

            bulletL = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/bulletL.gif"));
            bulletU = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/bulletU.gif"));
            bulletR = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/bulletR.gif"));
            bulletD = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/bulletD.gif"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
