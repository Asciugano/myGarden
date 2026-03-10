package com.asciugano.engine.entities;

import com.asciugano.engine.components.PointerComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.handlers.mouse.MouseHandler;
import org.joml.Vector3f;

public class Camera extends com.asciugano.game.entity.Entity {

    private Vector3f position;
    private float pitch = 20;
    private float yaw = 180;
    private float roll;
    private float distanceFromTarget = 50;
    private float angleAroundTarget;


    public Camera() {
        this.position = new Vector3f(-300, 5, -300);
        addComponent(new PointerComponent(this));
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundTarget();

        float horizontalDistance = calculateHorizontalDistanceFromTarget();
        float verticalDistance = calculateVerticalDistanceFromTarget();
        calculateCameraPosition(horizontalDistance, verticalDistance);

        Vector3f rotation = getTarget().getComponent(TransformationComponent.class).getRotation();
        yaw = 180 - (rotation.y + angleAroundTarget);
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
        com.asciugano.game.entity.Entity target = getComponent(PointerComponent.class).getTarget();
        if(target.getComponent(TransformationComponent.class) != null) {
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

    public com.asciugano.game.entity.Entity getTarget() { return getComponent(PointerComponent.class).getTarget(); }
//    public void setTarget(Entity target) { this.target = target; }
}