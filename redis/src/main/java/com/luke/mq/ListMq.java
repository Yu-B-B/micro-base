package com.luke.mq;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class ListMq {
    @Autowired
    private JedisPool jedisPool;

    public List<String> get() {
        Jedis jedis = null;
        // 创建连接
        try {
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.brpop(0, "");


        } catch (Exception e) {
            throw new RuntimeException("消息接收失败");
        } finally {
            jedis.close();
        }
    }

    public void set(String value,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(1);
            jedis.lpush(key,value);
        }catch (Exception e) {
            throw new RuntimeException("消息发送失败");
        }finally {
            jedis.close();
        }
    }
}
