package com.asciugano.engine.shaders;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.utils.Color;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;

public class TileShader extends ShaderProgram {

    private static final String PATH = "src/main/resources/shaders/";

    private static final String VERTEX_FILE = PATH + "tileVertexShader.glsl";
    private static final String FRAGMENT_FILE = PATH + "tileFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public TileShader() { super(VERTEX_FILE, FRAGMENT_FILE); }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "color");
        bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
    }

    public void loadProjectionMatrix(Matrix4f matrix) { super.loadMatrix(location_projectionMatrix, matrix); }
    public int getProgramID() {
        return super.getProgramID();
    }
}
