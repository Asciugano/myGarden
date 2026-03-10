package com.asciugano.game.scene;

import com.asciugano.game.entity.Entity;
import com.asciugano.game.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Terrain> terrains = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void addTerrain(Terrain terrain) {
        this.terrains.add(terrain);
    }

    public Terrain getTerrain(Terrain terrain) {
        return terrains.get(terrains.indexOf(terrain));
    }

    public Entity getEntity(Entity entity) {
        return entities.get(entities.indexOf(entity));
    }
}
