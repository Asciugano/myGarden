package com.asciugano.game.entity.tiles;

import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.models.QuadBuilder;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.terrains.EdgeVertexGenerator;
import com.asciugano.engine.utils.Color;
import com.asciugano.engine.utils.Maths;
import org.joml.Vector3f;

public class PathTile extends TerrainTile {

  private static final Color COLOR = new Color(new Vector3f(0.4f, 0.5f, 0.5f));
  private static final float HEIGHT = -0.2f;
  private static final float OFFSET = 0.1f;

  public PathTile(Tile tile, Loader loader) {
    super(tile, loader);
    super.bindModel(loader, COLOR);
  }

  @Override
  public float getEdgeHeight() {
    return HEIGHT;
  }

  @Override
  public Color getEdgeColor() {
    return COLOR;
  }

  @Override
  protected void generateMeshVertices(MeshBuilder meshBuilder) {
    QuadBuilder builder = new QuadBuilder(meshBuilder);

    // builder.addVertex(new Vector3f(0, HEIGHT, 0));
    // builder.addVertex(new Vector3f(0, HEIGHT, 1));
    // builder.addVertex(new Vector3f(1, HEIGHT, 1));
    // builder.addVertex(new Vector3f(1, HEIGHT, 0));
    // builder.addVertex(new Vector3f(OFFSET, 0, OFFSET));
    // builder.addVertex(new Vector3f(OFFSET, 0, 1 - OFFSET));
    // builder.addVertex(new Vector3f(1 - OFFSET, 0, 1 - OFFSET));
    // builder.addVertex(new Vector3f(1 - OFFSET, 0, OFFSET));
    // builder.generateQuad(4, 5, 6, 7, Maths.UP, COLOR);
    // builder.generateQuad(5, 1, 2, 6, COLOR);
    // builder.generateQuad(6, 2, 3, 7, COLOR);
    // builder.generateQuad(7, 3, 0, 4, COLOR);
    // builder.generateQuad(0, 3, 2, 1, COLOR);
    // builder.generateQuad(4, 0, 1, 5, COLOR);

    builder.generateQuad(
        new Vector3f(0, HEIGHT, 0),
        new Vector3f(0, HEIGHT, 1),
        new Vector3f(1, HEIGHT, 1),
        new Vector3f(1, HEIGHT, 0),
        COLOR);
    builder.generateQuad(
        new Vector3f(OFFSET, 0, OFFSET),
        new Vector3f(OFFSET, 0, 1 - OFFSET),
        new Vector3f(1 - OFFSET, 0, 1 - OFFSET),
        new Vector3f(1 - OFFSET, 0, OFFSET),
        COLOR);

    builder.generateQuad(
        new Vector3f(OFFSET, 0, 1 - OFFSET),
        new Vector3f(0, HEIGHT, 1),
        new Vector3f(1, HEIGHT, 1),
        new Vector3f(1 - OFFSET, 0, 1 - OFFSET),
        COLOR);
    builder.generateQuad(
        new Vector3f(1 - OFFSET, 0, 1 - OFFSET),
        new Vector3f(1, HEIGHT, 1),
        new Vector3f(1, HEIGHT, 0),
        new Vector3f(1 - OFFSET, 0, OFFSET),
        COLOR);
    builder.generateQuad(
        new Vector3f(1 - OFFSET, 0, OFFSET),
        new Vector3f(1, HEIGHT, 0),
        new Vector3f(0, HEIGHT, 0),
        new Vector3f(OFFSET, 0, OFFSET),
        COLOR);
    builder.generateQuad(
        new Vector3f(OFFSET, 0, OFFSET),
        new Vector3f(0, HEIGHT, 0),
        new Vector3f(0, HEIGHT, 1),
        new Vector3f(OFFSET, 0, 1 - OFFSET),
        COLOR);

    EdgeVertexGenerator.generateEdgeVertices(this, meshBuilder);
  }
}
