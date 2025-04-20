package com.luke.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BloomFilterGV {
    public static void main(String[] args) {
        long expectedInsertions = 100000;
        double fpp = 0.00004;

        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),expectedInsertions,fpp);

        bloomFilter.put("100001");
        bloomFilter.put("100002");
        bloomFilter.put("100003");
        bloomFilter.put("100004");

        System.out.println(bloomFilter.mightContain("100005"));
    }
}
