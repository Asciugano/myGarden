package com.asciugano.engine.components;

import org.joml.Vector3f;

public class TransformationComponent implements Component {
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public TransformationComponent(Vector3f position, Vector3f rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getPosition() { return position; }

    public Vector3f getRotation() { return rotation; }

    public float getScale() { return scale; }
}
