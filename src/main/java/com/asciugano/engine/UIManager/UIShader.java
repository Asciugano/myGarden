package com.asciugano.engine.UIManager;

import com.asciugano.engine.shaders.ShaderProgram;
import org.joml.Matrix4f;

public class UIShader extends ShaderProgram {

    private static final String VERTEX_SHADER = "src/main/resources/shaders/uiVertexShader.glsl";
    private static final String FRAGMENT_SHADER = "src/main/resources/shaders/uiFragmentShader.glsl";

    private int location_transformationMatrix;

    public UIShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
}
