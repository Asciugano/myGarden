package com.asciugano.game.entity.tiles;

import com.asciugano.engine.components.RenderComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.game.entity.Entity;
import com.asciugano.game.entity.tiles.components.TileComponent;
import com.asciugano.game.terrain.Terrain;
import org.joml.Vector3f;

public class Tile extends Entity {
    private int gridX, gridY;
    private TileType tileType;
    private TexturedModel model;

    public Tile(TileType tileType, int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.tileType = tileType;

        addComponent(new TileComponent(tileType, true, new TexturedModel(null, null)));
        addComponent(new TransformationComponent(Terrain.getPositionFromGrid(gridX, gridY), new Vector3f(0, 0, 0), 1));
        addComponent(new RenderComponent(this.getComponent(TileComponent.class).getModel()));
    }

    public int getGridX() { return gridX; }

    public void setGridX(int gridX) { this.gridX = gridX; }

    public int getGridY() { return gridY; }

    public void setGridY(int gridY) { this.gridY = gridY; }


    public void update(float delta) {
//        TODO: update
    }
}
