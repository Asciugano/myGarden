package com.asciugano.engine.terrains;

import com.asciugano.engine.memory.MemoryMapper;
import com.asciugano.engine.memory.VBOMemoryUpdater;
import com.asciugano.engine.models.MeshData;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.utils.Direction;
import com.asciugano.game.entity.tiles.*;
import com.asciugano.game.entity.tiles.chunks.Chunk;
import com.asciugano.game.entity.tiles.chunks.ChunkManager;

import org.joml.Vector3f;

public class Terrain {
  private static final int SIZE = 32;

  private static ChunkManager manager;
  private static VBOMemoryUpdater<Chunk> updater;

  public static int getSize() {
    return SIZE;
  }

  public Terrain(Loader loader) {
    initChunks(loader);
  }

  private void loadFromFile() {
    // TODO: implementare dopo al salvataggio
  }

  private void initChunks(Loader loader) {
    MeshData mesh = new MeshData(1024 * 1024);
    MemoryMapper<Chunk> mapper = new MemoryMapper<>();
    updater = new VBOMemoryUpdater<>(mesh, mapper);
    manager = new ChunkManager(updater, loader);

    for (int x = 0; x < SIZE; x++) {
      for (int z = 0; z < SIZE; z++) {
        manager.loadChunk(x, z);
      }
    }
  }

  public TerrainTile getTileFormWorld(Vector3f pos) {
    float worldX = pos.x + offset();
    float worldZ = pos.z + offset();

    int tileX = (int) Math.floor(worldX / Tile.getTileSize());
    int tileZ = (int) Math.floor(worldZ / Tile.getTileSize());

    int chunkX = tileX / Chunk.getSize();
    int chunkZ = tileZ / Chunk.getSize();

    int localX = tileX % Chunk.getSize();
    int localZ = tileZ % Chunk.getSize();

    return getChunk(chunkX, chunkZ).getTile(tileX, tileZ);
  }

  public Vector3f getPositionFromGrid(int chunkX, int chunkZ, int tileX, int tileZ) {
    float worldX = (chunkX * Chunk.getSize() + tileX) * Tile.getTileSize();
    float worldZ = (chunkZ * Chunk.getSize() + tileZ) * Tile.getTileSize();

    Chunk chunk = getChunk(chunkX, chunkZ);

    return new Vector3f(
        worldX - offset(),
        chunk.getTile(tileX, tileZ).getEdgeHeight(),
        worldZ - offset());
  }

  public static TerrainTile getTileNeightbour(TerrainTile tile, Direction direction, Chunk chunk) {
    int x = tile.getGridX();
    int z = tile.getGridZ();

    switch (direction) {
      case NORTH -> z -= 1;
      case SUD -> z += 1;
      case EAST -> x += 1;
      case WEST -> x -= 1;
    }

    if (x >= 0 || x < Chunk.getSize() || z >= 0 || z < Chunk.getSize())
      return chunk.getTile(x, z);

    int chunkX = chunk.getX();
    int chunkZ = chunk.getZ();

    if (x < 0) {
      chunkX--;
      x = Chunk.getSize() - 1;
    }
    if (z < 0) {
      chunkZ--;
      z = Chunk.getSize() - 1;
    }
    if (x > Chunk.getSize()) {
      chunkX++;
      x = 0;
    }
    if (z > Chunk.getSize()) {
      chunkZ++;
      z = 0;
    }

    Chunk neighbourChunk = getChunk(chunkX, chunkZ);

    return neighbourChunk.getTile(x, z);
  }

  private float offset() {
    return (SIZE * Chunk.getSize() * Tile.getTileSize()) / 2f;
  }

  public static Chunk getChunk(int x, int z) {
    return manager.getChunk(x + "," + z);
  }

  public void update() {
  }

  public ChunkManager getManager() {
    return manager;
  }

  public VBOMemoryUpdater<Chunk> getUpdater() {
    return updater;
  }
}
