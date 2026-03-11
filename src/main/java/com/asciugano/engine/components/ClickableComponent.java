package com.asciugano.engine.components;

import com.asciugano.engine.entities.Entity;

public class ClickableComponent implements Component {
    private Runnable onClick;

    public ClickableComponent(Runnable onClick) {
        this.onClick = onClick;
    }

    public ClickableComponent() {
        this.onClick = null;
    }


    public Runnable getOnClick() { return onClick; }

    public void setOnClick(Runnable onClick) { this.onClick = onClick; }
}
