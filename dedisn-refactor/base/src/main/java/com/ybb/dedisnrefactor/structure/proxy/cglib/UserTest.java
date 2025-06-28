package com.ybb.dedisnrefactor.structure.proxy.cglib;

import org.junit.jupiter.api.Test;

import java.util.List;

public class UserTest {
    @Test
    public void testCglib() {
        // 目标对象
        UserServerImpl server = new UserServerImpl();

        // 告知cglib将要代理的对象为 UserServerImpl
        UserServerImpl log = (UserServerImpl) new UserServerProxy().increase(server);

        log.setUser();

        List<User> users = log.getUsers();
        System.out.println(users);

        log.strongDetail();
    }
}
