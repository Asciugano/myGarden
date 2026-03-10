package com.asciugano.engine.components;

import com.asciugano.engine.entities.Entity;

public class ClickableComponent implements Component {
    private Entity entity;
    private Runnable onClick;

    public ClickableComponent(Entity entity, Runnable onClick) {
        this.entity = entity;
        this.onClick = onClick;
    }

    public ClickableComponent(Entity entity) {
        this.entity = entity;
        this.onClick = null;
    }

    public Entity getEntity() { return entity; }


    public Runnable getOnClick() { return onClick; }

    public void setOnClick(Runnable onClick) { this.onClick = onClick; }
}
