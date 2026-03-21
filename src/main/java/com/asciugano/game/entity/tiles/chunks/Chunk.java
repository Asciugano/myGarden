package com.asciugano.game.entity.tiles.chunks;

import java.nio.ByteBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;

import com.asciugano.engine.models.MeshData;
import com.asciugano.engine.renderer.Loader;
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

  private TerrainTile tiles[][];
  private MeshData meshData;

  private int x, z;

  public Chunk(int x, int z) {
    this.x = x;
    this.z = z;
  }

  public void loadTiles() {
    // TODO: implementare dopo salvataggi
  }

  public void generateTiles(Loader loader) {
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

    byte[] mesh = ChunkMeshBuilder.build(this);
    meshData = new MeshData(mesh.length);

    meshData.getMeshDataVBO().updateData(0, toBuffer(mesh));
    meshData.setVertexCount(mesh.length / MeshData.BYTES_PER_VERTEX);
  }

  private ByteBuffer toBuffer(byte[] data) {
    ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
    buffer.put(data);
    buffer.flip();

    return buffer;
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
}
