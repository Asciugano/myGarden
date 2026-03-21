package com.asciugano.game.entity.tiles.chunks;

import java.util.HashMap;
import java.util.Map;

import com.asciugano.engine.memory.VBOMemoryUpdater;
import com.asciugano.engine.renderer.Loader;

public class ChunkManager {

  private final Map<String, Chunk> chunks = new HashMap<>();
  private final VBOMemoryUpdater<Chunk> updater;
  private Loader loader;

  public ChunkManager(VBOMemoryUpdater<Chunk> updater, Loader loader) {
    this.updater = updater;
    this.loader = loader;
  }

  public void loadChunk(int x, int z) {
    Chunk chunk = new Chunk(x, z);
    chunk.generateTiles(loader);

    byte[] mesh = ChunkMeshBuilder.build(chunk);
    updater.store(chunk, mesh);
    chunks.put(key(x, z), chunk);
  }

  public void unloadChunk(int x, int z) {
    Chunk chunk = chunks.remove(key(x, z));
    if (chunk != null)
      updater.remove(chunk);
  }

  public Map<String, Chunk> getChunks() {
    return chunks;
  }

  public Chunk getChunk(String key) {
    return chunks.get(key);
  }

  private String key(int x, int z) {
    return x + "," + z;
  }
}
