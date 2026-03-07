package com.asciugano.engine.utils;

import com.asciugano.engine.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix(
            Vector3f translation,
            Vector3f rotation,
            float scale
    ) {
        return new Matrix4f()
                .identity()
                .translate(translation)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        Vector3f cameraPosition = camera.getPosition();
        Vector3f negativeCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        viewMatrix
                .identity()
                .rotateX((float) Math.toRadians(camera.getPitch()))
                .rotateY((float) Math.toRadians(camera.getYaw()))
                .rotateZ((float) Math.toRadians(camera.getRoll()))
                .translate(negativeCameraPosition);

        return viewMatrix;
    }
}
