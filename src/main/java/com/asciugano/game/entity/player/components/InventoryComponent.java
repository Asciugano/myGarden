package com.asciugano.game.entity.player.components;

import com.asciugano.engine.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent implements Component {
    List<Entity> items = new ArrayList<>();

    public Entity getItem(int id) { return items.get(id); }
    public void addItem(Entity entity) { items.add(entity); }
}
