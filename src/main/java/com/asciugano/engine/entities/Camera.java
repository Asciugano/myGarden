package com.asciugano.engine.entities;

import com.asciugano.engine.handlers.mouse.MouseHandler;
import com.asciugano.game.entity.Entity;
import org.joml.Vector3f;

public class Camera extends Entity {

    private Vector3f position;
    private float pitch = 20;
    private float yaw = 180;
    private float roll;
    private float distanceFromTarget = 50;
    private float angleAroundTarget;

    private Vector3f targetPoint;

    public Camera() {
        this.position = new Vector3f(-300, 5, -300);
        this.targetPoint = new Vector3f(0,0,0); // default
    }

    public void setTargetPoint(Vector3f targetPoint) {
        this.targetPoint = targetPoint;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundTarget();

        float horizontalDistance = calculateHorizontalDistanceFromTarget();
        float verticalDistance = calculateVerticalDistanceFromTarget();
        calculateCameraPosition(horizontalDistance, verticalDistance);
    }

    private void calculateZoom() {
        distanceFromTarget += MouseHandler.scroll;
    }

    private void calculateAngleAroundTarget() {
        if(MouseHandler.LEFT_PRESSED)
            angleAroundTarget -= MouseHandler.dx;
    }

    private void calculatePitch() {
        if(MouseHandler.RIGHT_PRESSED)
            pitch -= MouseHandler.dy;
    }

    private float calculateHorizontalDistanceFromTarget() {
        return (float)(distanceFromTarget * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistanceFromTarget() {
        return (float)(distanceFromTarget * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizDistance, float vertDistance) {
        float angle = angleAroundTarget;
        float xOffset = (float)(horizDistance * Math.sin(Math.toRadians(angle)));
        float zOffset = (float)(horizDistance * Math.cos(Math.toRadians(angle)));

        position.x = targetPoint.x - xOffset;
        position.z = targetPoint.z - zOffset;
        position.y = targetPoint.y + vertDistance;

        yaw = 180 - angle;
    }

    public Vector3f getPosition() { return position; }
    public float getPitch() { return pitch; }
    public float getYaw() { return yaw; }
    public float getRoll() { return roll; }


    public void update(float delta) {

    }
}