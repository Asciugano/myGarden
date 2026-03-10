package com.asciugano.engine.skyBox;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.shaders.ShaderProgram;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyBoxShader extends ShaderProgram {

    private static final String VERTEX_PATH = "src/main/resources/shaders/skyboxVertexShader.glsl";
    private static final String FRAGMENT_PATH = "src/main/resources/shaders/skyboxFragmentShader.glsl";
    private static final float ROTATE_SPEED = 1;

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;

    private float rotation;

    public SkyBoxShader() {
        super(VERTEX_PATH, FRAGMENT_PATH);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");

        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_blendFactor = super.getUniformLocation("blendFactor");
    }

    public void connectTextureUnits() {
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap2, 1);
    }

    public void loadBlendFactor(float factor) {
        super.loadFloat(location_blendFactor, factor);
    }

    public void loadFogColor(float r, float g, float b) {
        super.loadVector3(location_fogColor, new Vector3f(r, g, b));
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        rotation += ROTATE_SPEED * DisplayManager.getDelta();
        viewMatrix.rotateY((float) Math.toRadians(rotation));

        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }
}