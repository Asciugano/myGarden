package com.asciugano.engine.shaders;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class TileShader extends ShaderProgram {

    private static final int MAX_LIGHTS = 4;

    private static final String PATH = "src/main/resources/shaders/";

    private static final String VERTEX_FILE = PATH + "tileVertexShader.glsl";
    private static final String FRAGMENT_FILE = PATH + "tileFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    private int location_textureSampler;

    private int location_lightPosition;
    private int location_lightColor;
    private int location_attenuation;

    public TileShader() { super(VERTEX_FILE, FRAGMENT_FILE); }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");

        location_textureSampler = super.getUniformLocation("textureSampler");

            location_lightPosition = super.getUniformLocation("lightPosition");
            location_lightColor = super.getUniformLocation("lightColor");
            location_attenuation = super.getUniformLocation("attenuation");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLights(Light light) {
        super.loadVector3(location_lightPosition, light.getPosition());
        super.loadVector3(location_lightColor, light.getColor());
        super.loadVector3(location_attenuation, light.getAttenuation());
        }

    public void loadViewMatrix(Camera camera) {
        super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void connectTextureUnits() {
        super.loadInt(location_textureSampler, 0);
    }

    public int getProgramID() {
        return super.getProgramID();
    }
}
