package com.asciugano.game.scene;

import com.asciugano.engine.UIManager.UIEntity;
import com.asciugano.engine.UIManager.UIRenderer;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.EntityManager;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.handlers.mouse.MousePicker;
import com.asciugano.engine.memory.MemoryMapper;
import com.asciugano.engine.memory.VBOMemoryUpdater;
import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.models.MeshData;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.renderer.MasterRenderer;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.UI.TileSelector;
import com.asciugano.game.entity.tiles.chunks.Chunk;
import com.asciugano.game.entity.tiles.chunks.ChunkManager;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {
  private Light light;
  private Camera camera;
  private List<Terrain> terrains = new ArrayList<>();
  private List<Entity> entities = new ArrayList<>();

  private MasterRenderer masterRenderer;
  private UIRenderer uiRenderer;
  private List<UIEntity> uis = new ArrayList<>();
  private MousePicker mousePicker;
  private TileSelector tileSelector;

  private EntityManager entityManager = new EntityManager();
  private MeshData chunksMeshData;
  private VBOMemoryUpdater<Chunk> chunkUpdater;
  private ChunkManager chunkManager;

  public Scene(Loader loader) {
    terrains.add(new Terrain(loader));
    camera = new Camera();
    camera.setTarget(Terrain.getTileFromWorld(0, 0));
    light = new Light(new Vector3f(0, 10, 0), new Vector3f(1, 1, 1));

    initChunks(loader);

    this.masterRenderer = new MasterRenderer(loader);
    // TODO: fixare in futuro per quando si avranno piu terrains
    mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrains.get(0));

    // tileSelector = new TileSelector(
    // new UITexture(
    // loader.loadTexture("UITextures/selector.png"),
    // new Vector2f(0, 0),
    // new Vector2f(0.1f, 0.1f)),
    // Color.WHITE);
    // uiRenderer = new UIRenderer(loader);
    // uis.add(tileSelector);
    // SceneLayout.addEntitiesToScene(this);
  }

  private void initChunks(Loader loader) {
    chunksMeshData = new MeshData(1024 * 1024);
    MemoryMapper<Chunk> chunkMapper = new MemoryMapper<>();
    chunkUpdater = new VBOMemoryUpdater<>(chunksMeshData, chunkMapper);
    chunkManager = new ChunkManager(chunkUpdater, loader);

    chunkManager.loadChunk(0, 0);
    chunkManager.loadChunk(1, 0);
    chunkManager.loadChunk(0, 1);
    chunkManager.loadChunk(1, 1);
  }

  public Scene(Loader loader, Light light, Camera camera, List<Terrain> terrains, List<Entity> entities) {
    this.light = light;
    this.camera = camera;
    this.terrains = terrains;
    this.entities = entities;

    this.masterRenderer = new MasterRenderer(loader);
    // TODO: fixare in futuro per qundo si avranno piu terrains
    mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrains.get(0));

    // tileSelector = new TileSelector(
    // new UITexture(
    // loader.loadTexture("UITextures/selector.png"),
    // new Vector2f(0, 0),
    // new Vector2f(0.1f, 0.1f)),
    // Color.WHITE);
    // uiRenderer = new UIRenderer(loader);
    // uis.add(tileSelector);
    SceneLayout.addEntitiesToScene(this);
  }

  public void addEntity(Entity entity) {
    this.entities.add(entity);
  }

  public void addTerrain(Terrain terrain) {
    this.terrains.add(terrain);
  }

  public void update(float dt) {
    mousePicker.update();
    camera.move();
    for (UIEntity uiEntity : uis) {
      if (uiEntity == tileSelector)
        tileSelector.update(dt, mousePicker.getCurrentTile());
      uiEntity.update(dt);
    }

    for (Entity entity : entities) {
      entity.update();
    }
  }

  public Terrain getTerrain(Terrain terrain) {
    return terrains.get(terrains.indexOf(terrain));
  }

  public Entity getEntity(Entity entity) {
    return entities.get(entities.indexOf(entity));
  }

  public void render() {
    for (Terrain terrain : terrains) {
      masterRenderer.processTerrain(terrain);
    }
    for (Entity entity : entities) {
      masterRenderer.processEntity(entity);
    }

    masterRenderer.render(light, camera);
    // uiRenderer.render(uis);
  }

  public Light getLight() {
    return light;
  }

  public Camera getCamera() {
    return camera;
  }

  public List<Terrain> getTerrains() {
    return terrains;
  }

  public List<Entity> getEntities() {
    return entities;
  }

  public void cleanUp() {
    masterRenderer.cleanUp();
  }
}
