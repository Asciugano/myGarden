package com.asciugano.game.terrain;

import com.asciugano.game.entity.tiles.Tile;
import org.joml.Vector3f;

public class Terrain {
    private static Tile[][] tiles;

    public Terrain generateTerrain() {
        // TODO: implementazione seria
        return new Terrain();
    }

    public static Tile getTileFromWorld(float x, float y) {
        return tiles[(int) (x % com.asciugano.engine.terrains.Terrain.getSize())][(int) (y % com.asciugano.engine.terrains.Terrain.getSize())];
    }

    public static Vector3f getPositionFromGrid(int x, int y) {
        return new Vector3f(0, 0, 0);
    }
}
