package com.asciugano.engine.entities;

import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.handlers.mouse.MouseHandler;
import org.joml.Vector3f;

public class Camera extends Entity {

    private Vector3f position;
    private float pitch = 20;
    private static final float MAX_PITCH = 95;
    private static final float MIN_PITCH = 5;
    private float yaw = 180;
    private float roll;
    private float distanceFromTarget = 50;
    private static final float MIN_DISTANCE = 5;
    private float angleAroundTarget;
    private Entity target;


    public Camera() {
        this.position = new Vector3f(-300, 5, -300);
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundTarget();
        calculatePan();

        float horizontalDistance = calculateHorizontalDistanceFromTarget();
        float verticalDistance = calculateVerticalDistanceFromTarget();
        calculateCameraPosition(horizontalDistance, verticalDistance);

        yaw = 180 - angleAroundTarget;
    }

    public Vector3f getPosition() { return position; }

    public float getPitch() { return pitch; }

    public float getYaw() { return yaw; }

    public float getRoll() { return roll; }

    private void calculateZoom() {
        distanceFromTarget += MouseHandler.scroll;
        if(distanceFromTarget < MIN_DISTANCE) distanceFromTarget = MIN_DISTANCE;
    }

    private void calculateAngleAroundTarget() {
        if(MouseHandler.WHEEL_PRESSED)
            angleAroundTarget -= MouseHandler.dx;
    }

    private void calculatePan() {
        if(MouseHandler.LEFT_PRESSED) {
            position.x += MouseHandler.dx;
            position.y += MouseHandler.dy;

            // TODO: calcolare il Target sotto la cam
        }
    }

    private void calculatePitch() {
        if(MouseHandler.RIGHT_PRESSED)
            pitch -= MouseHandler.dy;

        if(pitch > MAX_PITCH) pitch = MAX_PITCH;
        if(pitch < MIN_PITCH) pitch = MIN_PITCH;
    }

    private float calculateHorizontalDistanceFromTarget() {
        return (float)(distanceFromTarget * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistanceFromTarget() {
        return (float)(distanceFromTarget * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizDistance, float vertDistance) {
        if(target != null) {
            if (target.getComponent(TransformationComponent.class) != null) {
                Vector3f rotation = target.getComponent(TransformationComponent.class).getRotation();
                float angle = rotation.y + angleAroundTarget;
                float xOffset = (float) (horizDistance * Math.sin(Math.toRadians(angle)));
                float zOffset = (float) (horizDistance * Math.cos(Math.toRadians(angle)));

                Vector3f targetPosition = target.getComponent(TransformationComponent.class).getPosition();
                position.x = targetPosition.x - xOffset;
                position.z = targetPosition.z - zOffset;

                position.y = targetPosition.y + vertDistance;
            }
        }
    }

    public Entity getTarget() { return target;}
    public void setTarget(Entity target) {
        this.target = target;
    }
}