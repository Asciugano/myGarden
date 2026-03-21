package com.asciugano.game.entity.tiles;

import com.asciugano.engine.terrains.Terrain;
import org.joml.Vector3f;

public class Tile {
  protected int gridX, gridZ;
  protected int chunkX, chunkZ;
  protected static final int TILE_SIZE = 4;

  public static int getTileSize() {
    return TILE_SIZE;
  }

  public Tile(int gridX, int gridZ, int chunkX, int chunkZ) {
    this.gridX = gridX;
    this.gridZ = gridZ;
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
  }

  public int getGridX() {
    return gridX;
  }

  public int getGridZ() {
    return gridZ;
  }

  public Vector3f getWorldPos() {
    float offset = Terrain.offset(gridX, gridZ);
    return new Vector3f(gridX * TILE_SIZE - offset, 0, gridZ * TILE_SIZE - offset);
  }

  public int getChunkX() {
    return chunkX;
  }

  public int getChunkZ() {
    return chunkZ;
  }
}
