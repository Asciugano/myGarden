package com.asciugano.engine.shaders;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class StaticShader extends ShaderProgram {
  private static final int MAX_LIGHTS = 4;

  private static final String PATH = "src/main/resources/shaders/";

  private static final String VERTEX_FILE = PATH + "vertexShader.glsl";
  private static final String FRAGMENT_FILE = PATH + "fragmentShader.glsl";

  private int location_transformationMatrix;
  private int location_projectionMatrix;
  private int location_viewMatrix;
  private int location_textureSampler;

  private int location_lightPosition;
  private int location_lightColor;
  private int location_attenuation;
  private int location_shineDamper;
  private int location_reflectivity;
  private int location_useFakeLighting;

  private int location_skyColor;

  private int location_numberOfRows;
  private int location_offset;

  public StaticShader() {
    super(VERTEX_FILE, FRAGMENT_FILE);
  }

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

    location_shineDamper = super.getUniformLocation("shineDamper");
    location_reflectivity = super.getUniformLocation("reflectivity");
    location_useFakeLighting = super.getUniformLocation("useFakeLighting");

    location_skyColor = super.getUniformLocation("skyColor");

    location_numberOfRows = super.getUniformLocation("numberOfRows");
    location_offset = super.getUniformLocation("offset");
  }

  public void loadNumberOfRows(int numberOfRows) {
    super.loadFloat(location_numberOfRows, numberOfRows);
  }

  public void loadOffset(Vector2f offset) {
    super.loadVector2(location_offset, offset);
  }

  public void loadSkyColor(float r, float g, float b) {
    super.loadVector3(location_skyColor, new Vector3f(r, g, b));
  }

  public void loadShineVariables(float damper, float reflectivity) {
    super.loadFloat(location_shineDamper, damper);
    super.loadFloat(location_reflectivity, reflectivity);
  }

  public void loadTransformationMatrix(Matrix4f matrix) {
    super.loadMatrix(location_transformationMatrix, matrix);
  }

  public void loadLights(Light light) {
    super.loadVector3(location_lightPosition, light.getPosition());
    super.loadVector3(location_lightColor, light.getColor());
    super.loadVector3(location_attenuation, light.getAttenuation());
  }

  public void loadFakeLighting(boolean useFakeLighting) {
    super.loadBoolean(location_useFakeLighting, useFakeLighting);
  }

  public void loadViewMatrix(Camera camera) {
    super.loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
  }

  public void loadProjectionMatrix(Matrix4f matrix) {
    super.loadMatrix(location_projectionMatrix, matrix);
  }

  public void connectTextureUnits() {
    super.loadFloat(location_textureSampler, 0);
  }

  public int getProgramID() {
    return super.getProgramID();
  }
}
