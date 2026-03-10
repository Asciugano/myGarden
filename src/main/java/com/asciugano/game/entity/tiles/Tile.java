package com.asciugano.game.entity.tiles;

import com.asciugano.engine.components.RenderComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.models.ModelData;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.renderer.OBJLoader;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.engine.textures.ModelTexture;
import com.asciugano.game.entity.tiles.components.TileComponent;
import org.joml.Vector3f;

public class Tile extends Entity {
    private static final int TILE_SIZE = 4;
    public static int getTileSize() { return TILE_SIZE; }

    private int gridX, gridY;
    private TileType tileType;
    private TexturedModel model;

    public Tile(TileType tileType, int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.tileType = tileType;

        Loader loader = new Loader();
        ModelData data = OBJLoader.loadOBJModel("tile");
        addComponent(new TileComponent(tileType, true, new TexturedModel(loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices()), new ModelTexture(loader.loadTexture("green.png")))));
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
