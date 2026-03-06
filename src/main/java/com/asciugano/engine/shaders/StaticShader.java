package com.asciugano.engine.shaders;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram {

    private static final String PATH = "src/main/resources/shaders/";

    private static final String VERTEX_FILE = PATH + "vertexShader.glsl";
    private static final String FRAGMENT_FILE = PATH + "fragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_textureSampler;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_textureSampler = super.getUniformLocation("textureSampler");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void connectTextureUnits() {
        super.loadFloat(location_textureSampler, 0);
    }

    public int getProgramID() {
        return super.getProgramID();
    }
}
