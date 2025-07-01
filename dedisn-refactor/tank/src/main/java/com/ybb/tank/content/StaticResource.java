package com.ybb.tank.content;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 静态资源管理
 */
public class StaticResource {
    public static BufferedImage tankL, tankU, tankR, tankD;

    // 加载静态资源，方便后续使用
    static {
        try {
            tankL = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankL.gif"));
            tankU = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankU.gif"));
            tankR = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankR.gif"));
            tankD = ImageIO.read(StaticResource.class.getClassLoader().getResourceAsStream("images/tankD.gif"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
