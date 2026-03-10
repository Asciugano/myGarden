package com.asciugano.game.entity.player;

import com.asciugano.engine.entities.Entity;
import com.asciugano.game.entity.player.components.InventoryComponent;

public class Player extends Entity {
    public Player() {
        addComponent(new InventoryComponent());
    }

    public void update(float delta) {

    }
}
