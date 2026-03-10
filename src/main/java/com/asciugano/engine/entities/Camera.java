package com.asciugano.engine.entities;

import com.asciugano.engine.handlers.mouse.MouseHandler;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch = 20;
    private float yaw = 180;
    private float roll;
    private float distanceFromTarget = 50;
    private float angleAroundTarget;

    private Entity target;

    public Camera() {
        this.target = null;
        this.position = new Vector3f(-300, 5, -300);
    }

    public Camera(Entity target) {
        this.target = target;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundTarget();

        float horizontalDistance = calculateHorizontalDistanceFromTarget();
        float verticalDistance = calculateVerticalDistanceFromTarget();
        calculateCameraPosition(horizontalDistance, verticalDistance);

        yaw = 180 - (target.getRotation().y + angleAroundTarget);
    }

    public Vector3f getPosition() { return position; }

    public float getPitch() { return pitch; }

    public float getYaw() { return yaw; }

    public float getRoll() { return roll; }

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
        float angle = target.getRotation().y + angleAroundTarget;
        float xOffset = (float)(horizDistance * Math.sin(Math.toRadians(angle)));
        float zOffset = (float)(horizDistance * Math.cos(Math.toRadians(angle)));

        position.x = target.getPosition().x - xOffset;
        position.z = target.getPosition().z - zOffset;

        position.y = target.getPosition().y + vertDistance;
    }

    public Entity getTarget() { return target; }
    public void setTarget(Entity target) { this.target = target; }
}
