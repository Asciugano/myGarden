package com.asciugano.game.entity;

import com.asciugano.engine.components.Component;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {
    private Map<Class<? extends  Component>, Component> components = new HashMap<>();

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public void update(float delta) { }
}
