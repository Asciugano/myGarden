package com.asciugano.engine.utils;

import com.asciugano.engine.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
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

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        return new Matrix4f()
                .identity()
                .translate(translation.x, translation.y, 0)
                .scale(new Vector3f(scale.x, scale.y, 1));
    }
}
