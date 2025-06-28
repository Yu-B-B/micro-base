package com.ybb.dedisnrefactor.structure.proxy.cglib;

import java.util.ArrayList;
import java.util.List;

public class UserServerImpl {
    private List<User> list = new ArrayList<>();

    public List<User> getUsers(){
        System.out.println("开始获取所有用户信息");
        return list;
    }

    public void setUser(){
        User user = new User();
        user.setAge(20);
        user.setName("yb");
        list.add(user);
        System.out.println("已添加一条数据");
    }

    public void strongDetail(){

    }
}
