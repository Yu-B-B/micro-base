package com.luke.bloom;


import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

// redisson实现布隆过滤器
public class BloomFilterRedisson {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");

        RedissonClient client = Redisson.create(config);
        RBloomFilter<String> bloomFilter = client.getBloomFilter("bloomFilter");
        bloomFilter.tryInit(100000000L,0.03); // 预算元素与误差率
        bloomFilter.add("100001");
        bloomFilter.add("100002");
        bloomFilter.add("100003");
        bloomFilter.add("100004");
        bloomFilter.add("100005");
        bloomFilter.add("100006");

        // 判断是否存在bloom过滤器中
        System.out.println(bloomFilter.contains("1111"));
        System.out.println(bloomFilter.contains("100001"));
    }
}
