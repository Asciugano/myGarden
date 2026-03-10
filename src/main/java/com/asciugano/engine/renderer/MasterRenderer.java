package com.asciugano.engine.renderer;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.shaders.TerrainShader;
import com.asciugano.engine.skyBox.SkyBoxRenderer;
import com.asciugano.engine.terrains.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;

    private static final float RED = 0.5f;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.5f;

    private Matrix4f projectionMatrix;
    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();
    private SkyBoxRenderer skyBoxRenderer;

    public MasterRenderer(Loader loader) {
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyBoxRenderer = new SkyBoxRenderer(loader, projectionMatrix);
    }

    private void renderTerrain(List<Light> lights, Camera camera) {
        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);

        terrainRenderer.render(terrains);

        terrainShader.stop();
    }
    private void renderEntity(List<Light> lights, Camera camera) {
        shader.start();
        shader.loadSkyColor(RED,GREEN,BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);

        entityRenderer.render(entities);

        shader.stop();
        entities.clear();
    }

    public void render(List<Light> lights, Camera camera) {
        prepare();

        renderTerrain(lights, camera);
        renderEntity(lights, camera);

        skyBoxRenderer.render(camera, new Vector3f(RED, GREEN, BLUE));

        terrains.clear();
        entities.clear();
    }

    public void processTerrains(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public static Vector3f getSkyColor() {
        return new Vector3f(RED,GREEN,BLUE);
    }

    public void prepare() {
        enableCulling();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }


    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glEnable(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();

        projectionMatrix = new Matrix4f().perspective(
                FOV,
                aspectRatio,
                NEAR_PLANE,
                FAR_PLANE
        );
    }

    public Matrix4f getProjectionMatrix() { return projectionMatrix; }
}
