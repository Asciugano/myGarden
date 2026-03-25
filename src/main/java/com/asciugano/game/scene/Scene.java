package com.asciugano.game.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.asciugano.engine.UIManager.UIEntity;
import com.asciugano.engine.UIManager.UIRenderer;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.EntityManager;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.handlers.mouse.MousePicker;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.renderer.MasterRenderer;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.UI.TileSelector;
import com.asciugano.game.entity.tiles.chunks.Chunk;

public class Scene {
  private Light light;
  private Camera camera;
  private Terrain terrain;
  private List<Entity> entities = new ArrayList<>();

  private MasterRenderer masterRenderer;
  private UIRenderer uiRenderer;
  private List<UIEntity> uis = new ArrayList<>();
  private MousePicker mousePicker;
  private TileSelector tileSelector;

  public EntityManager entityManager = new EntityManager();

  public Scene(Loader loader) {
    camera = new Camera();
    terrain = new Terrain(loader);
    camera.setTarget(Terrain.getChunk(16, 16).getTile(0, 0));
    light = new Light(new Vector3f(0, 10, 0), new Vector3f(1, 1, 1));

    this.masterRenderer = new MasterRenderer(loader);
    mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrain);

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

  public Scene(Loader loader, Light light, Camera camera, Terrain terrain, List<Entity> entities) {
    this.light = light;
    this.camera = camera;
    this.terrain = terrain;
    this.entities = entities;

    this.masterRenderer = new MasterRenderer(loader);
    mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrain);

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

  public void update(float dt) {
    mousePicker.update();
    camera.move();
    // for (UIEntity uiEntity : uis) {
    // if (uiEntity == tileSelector)
    // tileSelector.update(dt, mousePicker.getCurrentTile());
    // uiEntity.update(dt);
    // }

    for (Entity entity : entities) {
      entity.update();
    }

    terrain.update();
  }

  public Entity getEntity(Entity entity) {
    return entities.get(entities.indexOf(entity));
  }

  public void render() {
    masterRenderer.processTerrain(terrain);
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

  public List<Entity> getEntities() {
    return entities;
  }

  public void cleanUp() {
    masterRenderer.cleanUp();
    for (Chunk chunk : new ArrayList<Chunk>(terrain.getManager().getChunks().values())) {
      terrain.getManager().unloadChunk(chunk);
    }
  }
}
