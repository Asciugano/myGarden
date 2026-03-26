package com.asciugano.game.entity.tiles.chunks;

import java.nio.ByteBuffer;
import java.util.Random;

import org.joml.Vector3f;

import com.asciugano.engine.memory.VBOMemoryUpdater;
import com.asciugano.engine.models.MeshData;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.entity.tiles.DirtTile;
import com.asciugano.game.entity.tiles.GrassTile;
import com.asciugano.game.entity.tiles.PathTile;
import com.asciugano.game.entity.tiles.SoilTile;
import com.asciugano.game.entity.tiles.TerrainTile;
import com.asciugano.game.entity.tiles.Tile;

public class Chunk {

  private static final int SIZE = 16;

  public static int getSize() {
    return SIZE;
  }

  private TerrainTile tiles[][] = new TerrainTile[SIZE][SIZE];
  private MeshData meshData;

  private int x, z;
  private final String key;

  private int vertexOffset;
  private int vertexCount;

  public Chunk(int x, int z) {
    this.x = x;
    this.z = z;
    this.key = x + "," + z;
  }

  public void setRenderOffset(int vertexOffset, int vertexCount) {
    this.vertexOffset = vertexOffset;
    this.vertexCount = vertexCount;
  }

  public int getVertexCount() {
    return this.vertexCount;
  }

  public int getVertexOffset() {
    return this.vertexOffset;
  }

  public void loadTiles() {
    // TODO: implementare dopo salvataggi
  }

  public void generateTiles(Loader loader, VBOMemoryUpdater<Chunk> updater) {
    for (int x = 0; x < SIZE; x++) {
      for (int z = 0; z < SIZE; z++) {
        Random random = new Random();
        int typeN = random.nextInt(4);
        TerrainTile tile;

        switch (typeN) {
          case 0 -> tile = new PathTile(new Tile(x, z, this.x, this.z), loader);
          case 1 -> tile = new GrassTile(new Tile(x, z, this.x, this.z), loader);
          case 2 -> tile = new SoilTile(new Tile(x, z, this.x, this.z), loader);
          case 3 -> tile = new DirtTile(new Tile(x, z, this.x, this.z), loader);

          default -> tile = new GrassTile(new Tile(x, z, this.x, this.z), loader);
        }

        tiles[x][z] = tile;
      }
    }

    ByteBuffer mesh = ChunkMeshBuilder.build(this);
    updater.store(this, mesh);
    this.meshData = updater.getMeshData();
  }

  public TerrainTile getTile(int x, int z) {
    return tiles[x][z];
  }

  public TerrainTile[][] getTiles() {
    return tiles;
  }

  public int getX() {
    return x;
  }

  public int getZ() {
    return z;
  }

  public MeshData getMeshData() {
    return meshData;
  }

  public Vector3f getWorldPos() {
    float chunkSizeWorld = SIZE * Tile.getTileSize();
    return new Vector3f(
        x * chunkSizeWorld - Terrain.offset() + chunkSizeWorld / 2f,
        0,
        z * chunkSizeWorld - Terrain.offset() + chunkSizeWorld / 2f);
  }

  public String getKey() {
    return this.key;
  }
}
