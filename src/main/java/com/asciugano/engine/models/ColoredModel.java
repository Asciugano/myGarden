package com.asciugano.engine.models;

import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.utils.Color;
import org.joml.Vector3f;

public class ColoredModel {
    private RawModel model;
    private Color color;

    public ColoredModel(RawModel model, Color color) {
        this.model = model;
        this.color = color;
    }

    public RawModel getRawModel() { return model; }
    public Color getColor() { return color; }
}
