package com.asciugano.engine.renderer;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.shaders.TerrainShader;
import com.asciugano.engine.terrains.Terrain;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class MasterRenderer {


    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;
    private Matrix4f projectionMatrix;
    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer() {
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    private void renderTerrain(Light sun, Camera camera) {
        terrainShader.start();
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);

        terrainRenderer.render(terrains);

        terrainShader.stop();
    }
    private void renderEntity(Light sun, Camera camera) {
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);

        entityRenderer.render(entities);

        shader.stop();
        entities.clear();
    }

    public void render(Light sun, Camera camera) {
        prepare();

        renderTerrain(sun, camera);
        renderEntity(sun, camera);

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
}
