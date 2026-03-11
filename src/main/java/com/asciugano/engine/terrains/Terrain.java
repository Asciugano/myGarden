package com.asciugano.engine.terrains;

import com.asciugano.engine.renderer.Loader;
import com.asciugano.game.entity.tiles.Tile;
import com.asciugano.game.entity.tiles.TileType;
import org.joml.Vector3f;

public class Terrain {
    private static final int SIZE = 32;
    public static int getSize() { return SIZE; }

    private static Tile[][] tiles =  new Tile[SIZE][SIZE];

    public Terrain(Loader loader) {
        generateTerrain(loader);
    }

    private void loadFromFile() {
        // TODO: implementare dopo al salvataggio
    }

    public void generateTerrain(Loader loader) {
        for(int x = 0; x < SIZE; x++) {
            for(int z = 0; z < SIZE; z++) {
                TileType type = TileType.GRASS_TYPE;
                if(z % 2 == 0 || x % 2 == 0)
                    type = TileType.PATH_TYPE;

                tiles[x][z] = new Tile(loader, type, x, z);
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
