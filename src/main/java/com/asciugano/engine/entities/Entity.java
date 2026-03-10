package com.asciugano.engine.entities;

import com.asciugano.engine.models.TexturedModel;
import org.joml.Vector3f;

public class Entity {

    private TexturedModel model;

    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    private int textureIdx = 0;

    public Entity(
            TexturedModel model,
            Vector3f position,
            Vector3f rotation,
            float scale
    ) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(
            TexturedModel model,
            int textureIdx,
            Vector3f position,
            Vector3f rotation,
            float scale
    ) {
        this.model = model;
        this.textureIdx = textureIdx;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public float getTextureXOffset() {
        int column = textureIdx % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureIdx / model.getTexture().getNumberOfRows();
        return (float) row / (float) model.getTexture().getNumberOfRows();
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
