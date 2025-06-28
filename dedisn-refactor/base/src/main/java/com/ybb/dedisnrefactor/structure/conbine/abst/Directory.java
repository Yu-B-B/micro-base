package com.ybb.dedisnrefactor.structure.conbine.abst;

import java.util.ArrayList;

public class Directory extends Component{
    public ArrayList<Component> list = new ArrayList<>();
    @Override
    public void add(Component com) {
        list.add(com);
    }

    @Override
    public void remove(Component com) {
        list.remove(com);
    }

    @Override
    public Component getChild(int index) {
        return list.get(index);
    }

    @Override
    public void operate() {
        for (Component component : list) {
            component.operate();
        }
    }
}
