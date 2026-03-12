package com.asciugano.engine.components;

import com.asciugano.engine.entities.Entity;
import org.joml.Vector3f;

public class EntityTransform {
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public EntityTransform(Entity entity, Vector3f position, Vector3f rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public EntityTransform(Vector3f position) {
        this.position = position;
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = 1;
    }

    public Vector3f getPosition() { return position; }

    public Vector3f getRotation() { return rotation; }

    public float getScale() { return scale; }
}
