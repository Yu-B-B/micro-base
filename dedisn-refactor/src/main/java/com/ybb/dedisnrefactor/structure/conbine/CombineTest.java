package com.ybb.dedisnrefactor.structure.conbine;

import org.junit.jupiter.api.Test;

/**
 * 组合模式
 * 客户端 不用区分 文件还是文件夹
 */
public class CombineTest {
    @Test
    public void test1() {
        // 创建文件
        FileSystemComponent file1 = new Files("document.txt", 1024);
        FileSystemComponent file2 = new Files("image.jpg", 2048);

        // 创建子文件夹
        Folder subFolder = new Folder("Subfolder");
        subFolder.add(new Files("note.txt", 512));

        // 创建根文件夹
        Folder root = new Folder("Root");
        root.add(file1);
        root.add(file2);
        root.add(subFolder);

        // 统一操作
        root.display("");  // 显示整个结构
        System.out.println("Total size: " + root.getSize() + " bytes");
    }
}
