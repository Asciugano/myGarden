package com.asciugano.engine.components;

import com.asciugano.engine.entities.Entity;

public abstract class Component {
    public final ComponentType type;
    protected Entity entity;

    public Component(ComponentType type) { this.type = type; }

    public void setEntity(Entity entity) { this.entity = entity; }

    public void update() {}
}
