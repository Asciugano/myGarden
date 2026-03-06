package com.asciugano.engine.entities;

import com.asciugano.engine.models.TexturedModel;
import org.joml.Vector3f;

public class Entity {
    private TexturedModel model;

    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public Entity(
            TexturedModel model,
            Vector3f postion,
            Vector3f rotation,
            float scale
    ) {
        this.model = model;
        this.position = postion;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void increasePosition(Vector3f position) {
        this.position.add(position);
    }

    public void increaseRotation(Vector3f rotation) {
        this.rotation.add(rotation);
    }

    public TexturedModel getModel() { return model; }

    public void setModel(TexturedModel model) { this.model = model; }

    public Vector3f getPosition() { return position; }

    public void setPosition(Vector3f position) { this.position = position; }

    public Vector3f getRotation() { return rotation; }

    public void setRotation(Vector3f rotation) { this.rotation = rotation; }

    public float getScale() { return scale; }

    public void setScale(float scale) { this.scale = scale; }
}
