package com.ybb.dedisnrefactor.createType.creator.director;

import com.ybb.dedisnrefactor.createType.creator.builder.Builder;
import com.ybb.dedisnrefactor.createType.creator.product.Bike;

import java.util.ArrayList;
import java.util.List;

public class Director {

    private Builder mBuilder;

    public Director(Builder mBuilder) {
        this.mBuilder = mBuilder;
    }


    //自行车制作方法
    public Bike construct(){
        mBuilder.buildFrame();
        mBuilder.buildSeat();
        return mBuilder.createBike();
    }

    public static void main(String[] args) {
        String str = "25525511135";
        List<String> strings = restoreIpAddresses(str);
        System.out.println(strings);
    }

    public static List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        int n = s.length();
        if (n < 4 || n > 12) return result; // IP 地址最多 12 位
        for (int i = 1; i < Math.min(4, n - 2); i++) {
            for (int j = i + 1; j < Math.min(i + 4, n - 1); j++) {
                for (int k = j + 1; k < Math.min(j + 4, n); k++) {
                    if (i < n && j < n && k < n) {
                        String s1 = s.substring(0, i);
                        String s2 = s.substring(i, j);
                        String s3 = s.substring(j, k);
                        String s4 = s.substring(k);
                        if (isValid(s1) && isValid(s2) && isValid(s3) && isValid(s4)) {
                            result.add(s1 + "." + s2 + "." + s3 + "." + s4);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static boolean isValid(String segment) {
        if (segment.length() == 0 || segment.length() > 3) return false;
        if (segment.startsWith("0") && segment.length() > 1) return false; // 前导 0
        int val = Integer.parseInt(segment);
        return val >= 0 && val <= 255;
    }
}
