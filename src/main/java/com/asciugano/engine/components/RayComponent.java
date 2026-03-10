package com.asciugano.engine.components;

import org.joml.Vector3f;

public class RayComponent implements Component {
    private Vector3f origin;
    private Vector3f direction;
    private Vector3f hitPoint;

    public RayComponent(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
        this.hitPoint = new Vector3f();
    }

    public Vector3f getOrigin() { return origin; }

    public Vector3f getDirection() { return direction; }

    public Vector3f getHitPoint() { return hitPoint; }

    public void setHitPoint(Vector3f hitPoint) { this.hitPoint = hitPoint; }

    public void setOrigin(Vector3f origin) { this.origin = origin; }

    public void setDirection(Vector3f direction) { this.direction = direction; }
}
