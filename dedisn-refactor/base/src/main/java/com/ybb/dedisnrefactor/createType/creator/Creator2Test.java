package com.ybb.dedisnrefactor.createType.creator;

public class Creator2Test {
    public static void main(String[] args) {
        // 示例1：基本使用
        Createtor2 instance1 = new Createtor2.Builder()
                .setIp("192.168.1.1")
                .setPort(8080)
                .setType("HTTP")
                .setMessage("Hello World")
                .build();
        instance1.send();

        // 示例2：分步骤构建
        Createtor2.Builder builder = new Createtor2.Builder();
        builder.setIp("10.0.0.1");
        builder.setPort(9090);
        builder.setType("TCP");
        builder.setMessage("Connection request");
        Createtor2 instance2 = builder.build();
        instance2.send();

        // 示例3：复用构建器创建不同对象
        Createtor2.Builder baseBuilder = new Createtor2.Builder()
                .setIp("127.0.0.1")
                .setPort(3000);
        
        // 创建类型A的消息
        Createtor2 instance3 = baseBuilder
                .setType("TypeA")
                .setMessage("Message for Type A")
                .build();
        instance3.send();

        // 创建类型B的消息
        Createtor2 instance4 = baseBuilder
                .setType("TypeB")
                .setMessage("Message for Type B")
                .build();
        instance4.send();
    }
}
