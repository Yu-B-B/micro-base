package com.ybb.dedisnrefactor.structure.conbine.inter;

public class Files implements FileSystemComponent {
    private String name;
    private int size;

    public Files(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📄 " + name + " (" + size + " bytes)");
    }

    @Override
    public int getSize() {
        return this.size;
    }
}
