package com.ybb.tank.config;

import com.ybb.tank.content.StaticResource;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    static Properties properties = new Properties();
    public static final Integer SCREEN_HEIGHT ;
    public static final Integer SCREEN_WIDTH ;
    public static final String SCREEN_TITLE ;
    public static final Integer MY_TANK_DEFAULT_X ;
    public static final Integer MY_TANK_DEFAULT_Y ;
    public static final Integer MY_TANK_SPEED ;
    public static final Integer MY_TANK_SIZE ;
    public static final Integer BULLET_SPEED ;
    static {
        try {
            properties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("config.properties"));
            SCREEN_HEIGHT = PropertiesUtils.getInt("screenHeight");
            SCREEN_WIDTH = PropertiesUtils.getInt("screenWidth");
            SCREEN_TITLE = PropertiesUtils.getString("screenTitle");
            MY_TANK_DEFAULT_X = PropertiesUtils.getInt("myTankDefaultX");
            MY_TANK_DEFAULT_Y = PropertiesUtils.getInt("myTankDefaultY");
            MY_TANK_SPEED = PropertiesUtils.getInt("myTankSpeed");
            MY_TANK_SIZE = PropertiesUtils.getInt("myTankSize");
            BULLET_SPEED = PropertiesUtils.getInt("bulletSpeed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Object get(String key){
        if(properties == null) return null;
        return properties.get(key);
    }

    public static Integer getInt(String key){
        if(properties == null) return 0;
        return Integer.valueOf((String) properties.get(key));
    }

    public static String getString(String key){
        if(properties == null) return "";
        return (String) properties.get(key);
    }

}
