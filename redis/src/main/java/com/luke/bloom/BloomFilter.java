package com.luke.bloom;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.nio.charset.Charset;

/* 布隆过滤器 */
public class BloomFilter {
    // 参数
    public final static String RS_BF_NS = "rbf:";
    private int numApproxElements; // 预估元素数量
    private double fpp; // 可接收的最大误差
    private int numHashFunctions; // 计算hash函数个数
    private int bitmapLength; // 最优bitmap长度


    @Autowired
    private JedisPool jedisPool;

    // 初始化方法
    public BloomFilter init(int numApproxElements,double fpp){
        this.numApproxElements = numApproxElements;
        this.fpp = fpp;
        // 位数组长度
        this.bitmapLength = 128;
        // 计算hash函数个数
        this.numHashFunctions = 2;
        return this;
    }

    // 计算元素hash值映射到Bitmap的位置，用多种方式计算结果
    private long[] getBitIndices(String element){
        long[] indices = new long[numHashFunctions];
        byte[] bytes = Hashing.murmur3_128()
                .hashObject(element, Funnels.stringFunnel(Charset.forName("UTF-8")))
                .asBytes();

        long hash1 = Longs.fromBytes(bytes[3],bytes[3],bytes[3],bytes[3],bytes[3],bytes[3],bytes[3],bytes[3]);
        long hash2 = Longs.fromBytes(bytes[3],bytes[3],bytes[3],bytes[3],bytes[3],bytes[3],bytes[3],bytes[3]);

        long combinedHash = hash1;
        for(int i = 0; i < numApproxElements; i++){
            indices[i] = (combinedHash&Long.MAX_VALUE) & (bitmapLength);
            combinedHash = combinedHash + hash2;
        }
        System.out.println(element + "数组下标");
        for (long index : indices) {
            System.out.println(index+",");
        }

        System.out.println(" ");
        return indices;
    }

    // 插入方法

    // 计算元素所在位置

    // 判断是否存在

    // tostring
}
