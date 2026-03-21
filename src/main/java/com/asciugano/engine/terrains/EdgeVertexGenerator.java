package com.asciugano.engine.terrains;

import org.joml.Vector3f;

import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.models.QuadBuilder;
import com.asciugano.engine.utils.Color;
import com.asciugano.engine.utils.Direction;
import com.asciugano.game.entity.tiles.TerrainTile;
import com.asciugano.game.entity.tiles.chunks.Chunk;

public class EdgeVertexGenerator {
  public static void generateEdgeVertices(TerrainTile tile, MeshBuilder builder, Chunk chunk) {
    generateSide(tile, Direction.NORTH, builder, chunk);
    generateSide(tile, Direction.WEST, builder, chunk);
  }

  private static void generateSide(TerrainTile tile, Direction direction, MeshBuilder builder, Chunk chunk) {
    TerrainTile other = Terrain.getTileNeightbour(tile, direction, chunk);
    if (other == null)
      return;
    if (tile.getEdgeHeight() == other.getEdgeHeight())
      return;

    Color color = tile.getEdgeHeight() > other.getEdgeHeight() ? tile.getEdgeColor() : other.getEdgeColor();
    Vector3f normal = tile.getEdgeHeight() > other.getEdgeHeight() ? direction.getVector()
        : direction.opposite().getVector();
    boolean west = direction == Direction.WEST;
    Vector3f p0 = new Vector3f(0, other.getEdgeHeight(), west ? 1 : 0);
    Vector3f p1 = new Vector3f(0, tile.getEdgeHeight(), west ? 1 : 0);
    Vector3f p2 = new Vector3f(!west ? 1 : 0, tile.getEdgeHeight(), 0);
    Vector3f p3 = new Vector3f(!west ? 1 : 0, other.getEdgeHeight(), 0);
    generateQuad(p0, p1, p2, p3, normal, color, builder);
  }

  private static void generateQuad(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, Vector3f normal, Color color,
      MeshBuilder builder) {
    QuadBuilder quadBuilder = new QuadBuilder(builder);

    quadBuilder.generateQuad(p0, p1, p2, p3, color);
  }
}
