package com.asciugano.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.entities.MeshComponent;
import com.asciugano.engine.models.MeshData;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.shaders.TerrainShader;
import com.asciugano.engine.shaders.TileShader;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.entity.tiles.chunks.Chunk;

public class MasterRenderer {
  private static final float FOV = 70;
  private static final float NEAR_PLANE = 0.1f;
  private static final float FAR_PLANE = 1000.0f;

  private static final float RED = 0.5f;
  private static final float GREEN = 0.5f;
  private static final float BLUE = 0.5f;

  private Matrix4f projectionMatrix;
  private StaticShader shader = new StaticShader();
  private TerrainShader terrainShader = new TerrainShader();
  private TileShader tileShader = new TileShader();
  private EntityRenderer entityRenderer;

  private TileRenderer tileRenderer;
  private ChunkRenderer chunkRenderer;

  private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
  // private Map<ColoredModel, List<TerrainTile>> tiles = new HashMap<>();
  private List<MeshData> chunks = new ArrayList<>();
  // private SkyBoxRenderer skyBoxRenderer;

  public MasterRenderer(Loader loader) {
    createProjectionMatrix();
    entityRenderer = new EntityRenderer(shader, projectionMatrix);
    tileRenderer = new TileRenderer(tileShader, projectionMatrix);
    // skyBoxRenderer = new SkyBoxRenderer(loader, projectionMatrix);
  }

  private void renderEntity(Light light, Camera camera) {
    shader.start();
    shader.loadSkyColor(RED, GREEN, BLUE);
    shader.loadLights(light);
    shader.loadViewMatrix(camera);

    entityRenderer.render(entities);

    shader.stop();
  }

  private void renderTile(Light light, Camera camera) {
    tileShader.start();
    tileShader.loadViewMatrix(camera);
    tileShader.loadLight(light);

    tileRenderer.render(tiles);

    tileShader.stop();
  }

  private void renderChunk() {
    chunkRenderer.render(chunks);
  }

  public void render(Light light, Camera camera) {
    prepare();

    renderEntity(light, camera);
    // renderTile(light, camera);
    renderChunk();

    // skyBoxRenderer.render(camera, new Vector3f(RED, GREEN, BLUE));

    entities.clear();
    // tiles.clear();
    chunks.clear();
  }

  public void processTerrain(Terrain terrain) {
    for (Chunk chunk : terrain.getManager().getChunks().values()) {
      MeshData meshData = chunk.getMeshData();
      chunks.add(meshData);
    }
    // for (TerrainTile[] tilesA : terrain.getTiles()) {
    // for (TerrainTile tile : tilesA) {
    // ColoredModel model = tile.getModel();
    // List<TerrainTile> batch = tiles.get(model);
    // if (batch != null) {
    // batch.add(tile);
    // } else {
    // List<TerrainTile> newBatch = new ArrayList<>();
    // newBatch.add(tile);
    // tiles.put(model, newBatch);
    // }
    // }
    // }
  }

  public void processEntity(Entity entity) {
    MeshComponent meshComponent = new MeshComponent();
    if (entity.hasComponent(meshComponent.type)) {
      MeshComponent entityMC = ((MeshComponent) entity.getComponent(meshComponent.type));
      TexturedModel entityModel = entityMC.getModel();
      List<Entity> batch = entities.get(entityModel);
      if (batch != null) {
        batch.add(entity);
      } else {
        List<Entity> newBatch = new ArrayList<>();
        newBatch.add(entity);
        entities.put(entityModel, newBatch);
      }
    }
  }

  public static Vector3f getSkyColor() {
    return new Vector3f(RED, GREEN, BLUE);
  }

  public void prepare() {
    enableCulling();
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public void cleanUp() {
    shader.cleanUp();
    tileShader.cleanUp();
    terrainShader.cleanUp();
  }

  public static void enableCulling() {
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);
  }

  public static void disableCulling() {
    glDisable(GL_CULL_FACE);
  }

  private void createProjectionMatrix() {
    float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();

    projectionMatrix = new Matrix4f().perspective(
        (float) Math.toRadians(FOV),
        aspectRatio,
        NEAR_PLANE,
        FAR_PLANE);
  }

  public Matrix4f getProjectionMatrix() {
    return projectionMatrix;
  }
}
