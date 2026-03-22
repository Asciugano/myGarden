package com.asciugano.game.entity.tiles.chunks;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.entity.tiles.TerrainTile;

public class ChunkMeshBuilder {

  public static ByteBuffer build(Chunk chunk) {
    List<Float> vertices = new ArrayList<>();

    TerrainTile[][] tiles = chunk.getTiles();
    int size = Chunk.getSize();

    for (int x = 0; x < size; x++) {
      for (int z = 0; z < size; z++) {
        TerrainTile tile = tiles[x][z];
        addTileQuad(vertices, tile, chunk);
      }
    }

    ByteBuffer buffer = BufferUtils.createByteBuffer(vertices.size() * Float.BYTES);
    for (float f : vertices)
      buffer.putFloat(f);
    buffer.flip();

    return buffer;
  }

  private static void addTileQuad(List<Float> vertices, TerrainTile tile, Chunk chunk) {

    Vector3f position = Terrain.getPositionFromGrid(
        tile.getGridX(), tile.getGridZ(),
        chunk.getX(), chunk.getZ());

    float y = tile.getEdgeHeight();

    float r = tile.getEdgeColor().color.x;
    float g = tile.getEdgeColor().color.y;
    float b = tile.getEdgeColor().color.z;

    float nx = 0;
    float ny = 1;
    float nz = 0;

    addVertex(vertices, position.x, y, position.z, r, g, b, nx, ny, nz);
    addVertex(vertices, position.x + 1, y, position.z, r, g, b, nx, ny, nz);
    addVertex(vertices, position.x + 1, y, position.z + 1, r, g, b, nx, ny, nz);

    addVertex(vertices, position.x, y, position.z, r, g, b, nx, ny, nz);
    addVertex(vertices, position.x + 1, y, position.z + 1, r, g, b, nx, ny, nz);
    addVertex(vertices, position.x, y, position.z + 1, r, g, b, nx, ny, nz);
  }

  private static void addVertex(List<Float> v,
      float x, float y, float z,
      float r, float g, float b,
      float nx, float ny, float nz) {

    v.add(x);
    v.add(y);
    v.add(z);
    v.add(r);
    v.add(g);
    v.add(b);
    v.add(nx);
    v.add(ny);
    v.add(nz);
  }

  private static ByteBuffer toByteBuffer(List<Float> data) {
    ByteBuffer buffer = BufferUtils.createByteBuffer(data.size() * Float.BYTES);
    for (float f : data)
      buffer.putFloat(f);

    return buffer.flip();
  }
}
