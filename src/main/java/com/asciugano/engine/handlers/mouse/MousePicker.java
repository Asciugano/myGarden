package com.asciugano.engine.handlers.mouse;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.engine.utils.Maths;
import com.asciugano.game.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MousePicker {
    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600f;

    private Vector3f currentRay = new Vector3f();
    private Vector3f currentTerrainPoint;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;
    private Terrain terrain;

    public MousePicker(Camera camera, Matrix4f projectionMatrix, Terrain terrain) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = Maths.createViewMatrix(camera);
        this.terrain = terrain;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Entity getCurrentEntity() {
        return com.asciugano.game.terrain.Terrain.getTileFromWorld(currentTerrainPoint.x, currentTerrainPoint.z);
    }

    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();

        if (intersectionInRange(0f, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0f, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = new Vector3f(0, 0, 0);
        }
    }

    private Vector3f calculateMouseRay() {
        float mouseX = clamp(MouseHandler.mouseX, 0, DisplayManager.getWidth());
        float mouseY = clamp(MouseHandler.mouseY, 0, DisplayManager.getHeight());

        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        return toWorldCoords(eyeCoords);
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
        float x = (2f * mouseX) / DisplayManager.getWidth() - 1f;
        float y = 1f - (2f * mouseY) / DisplayManager.getHeight(); // y invertito per OpenGL
        return new Vector2f(x, y);
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f(projectionMatrix).invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = new Matrix4f(viewMatrix).invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        return new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z).normalize();
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        return new Vector3f(camera.getPosition()).add(new Vector3f(ray).mul(distance));
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            return getTerrain(endPoint.x, endPoint.z) != null ? endPoint : null;
        }

        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    private boolean isUnderGround(Vector3f point) {
        Terrain terrain = getTerrain(point.x, point.z);
        if (terrain == null) return false;
//        float height = terrain.getHeightOfTerrain(point.x, point.z);
        float height = 0;
        return point.y < height;
    }

    private Terrain getTerrain(float x, float z) {
        return terrain;
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
