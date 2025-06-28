package com.ybb.dedisnrefactor.createType.single;

import java.io.*;

public class SingleSerial {
    /**
     * 结果返回为false，两个对象不一致，通过serializable创建的对象最后还是使用反射创建的对象
     */
    public static void main(String[] args) throws Exception {
        //序列化对象输出流
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile.obj"));
        oos.writeObject(SingletonEnum.getInstance());

        //序列化对象输入流
        File file = new File("tempFile.obj");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        // readObject --> readObject0 --> checkResolve(readOrdinaryObject(unshared)) --> readOrdinaryObject
        // --> obj = desc.isInstantiable() ? desc.newInstance() : null;
        SingletonEnum single = (SingletonEnum) ois.readObject();

        System.out.println(single);
        System.out.println(single.getInstance());


        //判断是否是同一个对象
        System.out.println(single.getInstance() == single);//false

    }
}

class Singleton implements Serializable {
    private static Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        if(instance == null) {
            synchronized (Singleton.class) {
                if(instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}