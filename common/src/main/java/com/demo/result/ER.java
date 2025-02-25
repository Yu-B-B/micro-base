package com.demo.result;

import lombok.Data;

@Data
public class ER {
    private Integer code;
    private String msg;
    private Object data;

    public static ER ok() {
        ER er = new ER();
        er.setCode(200);
        return er;
    }

    public static ER ok(String msg, Object data) {
        ER er = new ER();
        er.setMsg(msg);
        er.setData(data);
        return er;
    }

    public static ER error() {
        ER er = new ER();
        er.setCode(500);
        return er;
    }

    public static ER error(Integer code, String msg) {
        ER er = new ER();
        er.setCode(code);
        er.setMsg(msg);
        return er;
    }


}
