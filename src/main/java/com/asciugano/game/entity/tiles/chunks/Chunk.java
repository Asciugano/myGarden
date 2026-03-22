package com.asciugano.game.entity.tiles.chunks;

import java.nio.ByteBuffer;
import java.util.Random;

import org.joml.Vector3f;

import com.asciugano.engine.models.MeshData;
import com.asciugano.engine.models.RawModel;
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
  private RawModel model;

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

    ByteBuffer mesh = ChunkMeshBuilder.build(this);
    meshData = new MeshData(mesh.capacity());

    meshData.getMeshDataVBO().updateData(0, mesh);
    meshData.setVertexCount(mesh.capacity() / MeshData.BYTES_PER_VERTEX);

    genModel(loader, mesh);
  }

  private void genModel(Loader loader, ByteBuffer mesh) {
    int floatCount = mesh.capacity() / Float.BYTES;
    float[] vertexData = new float[floatCount];
    mesh.asFloatBuffer().get(vertexData);

    int floatPerVertex = MeshData.BYTES_PER_VERTEX / Float.BYTES;
    int vertexNum = vertexData.length / floatPerVertex;

    float[] positions = new float[vertexNum * 3];
    float[] colors = new float[vertexNum * 3];
    float[] normals = new float[vertexNum * 3];

    for (int i = 0; i < vertexNum; i++) {
      int base = i * (MeshData.BYTES_PER_VERTEX / Float.BYTES);

      positions[i * 3] = vertexData[base];
      positions[i * 3 + 1] = vertexData[base + 1];
      positions[i * 3 + 2] = vertexData[base + 2];

      colors[i * 3] = vertexData[base + 3];
      colors[i * 3 + 1] = vertexData[base + 4];
      colors[i * 3 + 2] = vertexData[base + 5];

      normals[i * 3] = vertexData[base + 6];
      normals[i * 3 + 1] = vertexData[base + 7];
      normals[i * 3 + 2] = vertexData[base + 8];
    }

    int[] indices = new int[vertexNum];
    for (int i = 0; i < vertexNum; i++)
      indices[i] = i;

    this.model = loader.loadToVAO(positions, colors, 3, normals, indices);
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

  public RawModel getModel() {
    return model;
  }

  public Vector3f getWorldPos() {
    float chunkSizeWorld = SIZE * Tile.getTileSize();
    return new Vector3f(
        x * chunkSizeWorld - Terrain.offset() + chunkSizeWorld / 2f,
        0,
        z * chunkSizeWorld - Terrain.offset() + chunkSizeWorld / 2f);
  }
}
