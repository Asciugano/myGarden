package com.asciugano.engine.entities;

import com.asciugano.engine.components.Component;
import com.asciugano.engine.components.ComponentType;
import com.asciugano.engine.components.EntityTransform;
import com.asciugano.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Entity {
    public final int id;

    public final Blueprint blueprint;
    public final Scene scene;
    public final EntityTransform transform;

    private Map<ComponentType, Component> components;
    private List<Component> activeComponents = new ArrayList<>();

    public Entity(Blueprint blueprint, Scene scene, EntityTransform transform) {
        this.id = scene.entityManager.nextEntityID();
        this.blueprint = blueprint;
        this.scene = scene;
        this.transform = transform;

        transform.setEntity(this);
        scene.entityManager.addEntity(this);
    }

    public void setComponents(Map<ComponentType, Component> components) {
        this.components = components;
        getActiveComponents();
    }

    public boolean hasComponent(ComponentType type) {
        return getComponent(type) != null;
    }

    public Component getComponent(ComponentType type) {
        return components.get(type);
    }

    public void update() {
        for(Component component : activeComponents) {
            component.update();
        }
    }

    public void getActiveComponents() {
        for(Component component : components.values()) {
            if(component.type.isActive) {
                activeComponents.add(component);
            }
        }
    }


}
