package com.asciugano.engine.components;

import java.util.ArrayList;
import java.util.List;

public class ComponentUser {
    protected List<Component> components = new ArrayList<>();

    public void addComponent(Component component) {
        components.add(component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if(componentClass.isInstance(component)) return componentClass.cast(component);
        }
        return null;
    }
}
