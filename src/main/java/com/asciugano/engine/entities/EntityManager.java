package com.asciugano.engine.entities;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private int nextID = 0;
    private List<Entity> entities = new ArrayList<>();

    public int nextEntityID() { return nextID++; }
    public void addEntity(Entity entity) { entities.add(entity); }

    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public List<Entity> getEntities() { return entities; }
}
