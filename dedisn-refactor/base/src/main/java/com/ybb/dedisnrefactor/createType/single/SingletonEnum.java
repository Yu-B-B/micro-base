package com.ybb.dedisnrefactor.createType.single;

public enum SingletonEnum {
    INSTANCE;

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static SingletonEnum getInstance(){

        return INSTANCE;
    }
}

