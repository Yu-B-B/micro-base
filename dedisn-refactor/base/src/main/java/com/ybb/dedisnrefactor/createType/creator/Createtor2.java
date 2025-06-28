package com.ybb.dedisnrefactor.createType.creator;

public class Createtor2 {
    private final String ip;
    private final int port;
    private final String type;
    private final String message;

    // builder为目标类的参数
    public Createtor2(Builder builder){
        this.ip = builder.ip;
        this.port = builder.port;
        this.type = builder.type;
        this.message = builder.message;
    }

    // builder位于目标类内部
    public static class Builder{
        // builder 中参数要提供set方法，内部设置，保证密闭性
        private String ip;
        private int port;
        private String type;
        private String message;

        // builder 中的setter方法返回当前对象
        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        // builder对外提供一个构建方法，实现目标类创建
        public Createtor2 build(){
            // 内容校验
            if (ip == null || ip.isEmpty()) {
                throw new IllegalArgumentException("IP cannot be empty");
            }
            if (port <= 0) {
                throw new IllegalArgumentException("Port must be positive");
            }
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("Type cannot be empty");
            }
            if (message == null || message.isEmpty()) {
                throw new IllegalArgumentException("Message cannot be empty");
            }
            return new Createtor2(this);
        }
    }

    public void send(){
        System.out.println("向 " + ip + ":" + port + " 发送了一条类型为 " + type + " 的消息: " + message);
    }
}
