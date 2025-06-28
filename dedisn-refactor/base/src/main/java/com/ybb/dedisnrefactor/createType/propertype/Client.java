package com.ybb.dedisnrefactor.createType.propertype;

import java.util.Random;

public class Client {
    private static int MAX_COUNT = 5;

    private static void batchSend(Mail2 mail) {
        System.out.println("发件人："+mail.getFrom()+"标题："+mail.getSubject()+"\n内容:"+mail.getContent()+"\n收件人："+mail.getTo()+"\n");
    }

    public static void main(String[] args) {
        Mail2 mail = new Mail2(new Template());

        mail.setFrom("有才银行\n");
        while (MAX_COUNT-- > 0) {
            Mail2 clone = mail.clone(); // 通过对象拷贝，减少对象创建
            Random r = new Random();
            int i = r.nextInt(100000);
            clone.setTo(i+"@163.com");
            batchSend(clone);
        }
    }
}
