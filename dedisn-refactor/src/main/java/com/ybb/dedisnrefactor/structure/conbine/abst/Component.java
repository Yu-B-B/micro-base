package com.ybb.dedisnrefactor.structure.conbine.abst;

public abstract class Component {
    public abstract void add(Component com);

    public abstract void remove(Component com);

    public abstract Component getChild(int index);

    public abstract void operate();
}
