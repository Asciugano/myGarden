package com.asciugano.engine.components;

import com.asciugano.engine.models.TexturedModel;

public class RenderComponent implements Component {
    public TexturedModel model;

    public RenderComponent(TexturedModel model) {
        this.model = model;
    }

    public TexturedModel getModel() { return model; }

    public void setModel(TexturedModel model) { this.model = model; }
}
