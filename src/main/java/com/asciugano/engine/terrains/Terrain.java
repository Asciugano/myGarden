package com.asciugano.engine.terrains;

import com.asciugano.game.entity.tiles.Tile;
import com.asciugano.game.entity.tiles.TileType;
import com.asciugano.game.entity.tiles.components.TileComponent;
import org.joml.Vector3f;

import java.util.LinkedList;

public class Terrain {
    private static final int SIZE = 32;
    public static int getSize() { return SIZE; }

    private static Tile[][] tiles =  new Tile[SIZE][SIZE];

    public Terrain() {
        generateTerrain();
    }

    private void loadFromFile() {
        // TODO: implementare dopo al salvataggio
    }

    public void generateTerrain() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                tiles[x][y] = new Tile(TileType.GRASS_TYPE, x, y);
            }
        }
    }

    public static Vector3f getPositionFromGrid(int x, int z) {
        return new Vector3f(x * Tile.getTileSize(), 0, z * Tile.getTileSize());
    }

    public static Tile getTileFromWorld(float x, float z) {
        int gridX = (int) (x / Tile.getTileSize());
        int gridZ = (int) (z / Tile.getTileSize());

        if(gridX < 0 || gridZ < 0 || gridX >= SIZE || gridZ >= SIZE) {
            return null;
        }

        return tiles[gridX][gridZ];
    }

    public Tile[][] getTiles() { return tiles; }
}
