package com.asciugano.game.entity.tiles;

import com.asciugano.engine.components.ClickableComponent;
import com.asciugano.engine.components.ComponentUser;
import com.asciugano.engine.components.RenderComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.engine.textures.ModelTexture;
import org.joml.Vector3f;

public class Tile extends ComponentUser {
    private static final int TILE_SIZE = 4;
    public static int getTileSize() { return TILE_SIZE; }

    private int gridX, gridZ;
    private TileType tileType;
    private TexturedModel model;

    public Tile(Loader loader, TileType tileType, float x, float z, int gridX, int gridZ) {
        this.gridX = (int) (x / Tile.getTileSize() + ((float) (Terrain.getSize() * TILE_SIZE) / 2));
        this.gridZ = (int) (x / Tile.getTileSize() + ((float) (Terrain.getSize() * TILE_SIZE) / 2));
        this.tileType = tileType;

        this.model = createModel(loader);
        addComponent(new TransformationComponent(new Vector3f(x, 0, z), new Vector3f(0, 0, 0), TILE_SIZE));
        addComponent(new ClickableComponent(this::onClick));
        addComponent(new RenderComponent(model));
    }

    public Tile(Loader loader, TileType tileType, int gridX, int gridZ) {
        this.gridX = gridX;
        this.gridZ = gridZ;
        this.tileType = tileType;

        this.model = createModel(loader);

        addComponent(new TransformationComponent(Terrain.getPositionFromGrid(gridX, gridZ), new Vector3f(0, 0, 0), TILE_SIZE));
        addComponent(new ClickableComponent(this::onClick));
        addComponent(new RenderComponent(model));
    }

    private TexturedModel createModel(Loader loader) {
        float[] vertices = {
                0, 0, 0,
                0, 0, 1,
                1, 0, 1,
                1, 0, 0
        };

        float[] textureCoords = {
                0, 0, 0, 1, 1, 1, 1, 0
        };

        int[] indices = { 0, 1, 2, 2, 3, 0 };
        float[] normals = {
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
        };

        return new TexturedModel(
                loader.loadToVAO(
                        vertices, normals, textureCoords,  indices),
                new ModelTexture( loader.loadTexture(tileType.getTextureName()))
        );
    }

    protected void onClick() {
        System.out.println("click the tile: " + gridX + ", " + gridZ);
    }

    public int getGridX() { return gridX; }

    public void setGridX(int gridX) { this.gridX = gridX; }

    public int getGridZ() { return gridZ; }

    public void setGridZ(int gridZ) { this.gridZ = gridZ; }
}
