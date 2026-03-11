package com.asciugano.game.scene;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.handlers.mouse.MousePicker;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.renderer.MasterRenderer;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.entity.tiles.Tile;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Light light;
    private Camera camera;
    private List<Terrain> terrains = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();

    private MasterRenderer masterRenderer;
    private MousePicker mousePicker;

    public Scene(Loader loader) {
        terrains.add(new Terrain(loader));
        camera = new Camera();
        camera.setTarget(Terrain.getTileFromWorld(0, 0));
        light = new Light(new Vector3f(0, 100, 100), new Vector3f(1, 1, 1));

        this.masterRenderer = new MasterRenderer(loader);
        // TODO: fixare in futuro per quando si avranno piu terrains
        mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrains.get(0));
    }

    public Scene(Loader loader, Light light, Camera camera, List<Terrain> terrains, List<Entity> entities) {
        this.light = light;
        this.camera = camera;
        this.terrains = terrains;
        this.entities = entities;

        this.masterRenderer = new MasterRenderer(loader);
        // TODO: fixare in futuro per qundo si avranno piu terrains
        mousePicker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), terrains.get(0));
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

        for(Entity entity : entities) {
            entity.update(dt);
        }
    }
    public Terrain getTerrain(Terrain terrain) {
        return terrains.get(terrains.indexOf(terrain));
    }

    public Entity getEntity(Entity entity) {
        return entities.get(entities.indexOf(entity));
    }

    public void render() {
        for(Terrain terrain : terrains) {
            masterRenderer.processTerrain(terrain);
        }
        for(Entity entity : entities) {
            masterRenderer.processEntity(entity);
        }

        masterRenderer.render(light, camera);
    }

    public Light getLight() { return light; }

    public Camera getCamera() { return camera; }

    public List<Terrain> getTerrains() { return terrains; }

    public List<Entity> getEntities() { return entities; }

    public void cleanUp() {
        masterRenderer.cleanUp();
    }
}
