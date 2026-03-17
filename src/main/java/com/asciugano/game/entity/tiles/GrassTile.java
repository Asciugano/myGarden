package com.asciugano.game.entity.tiles;

import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.models.QuadBuilder;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.utils.Color;
import com.asciugano.engine.utils.Maths;
import org.joml.Vector3f;

public class GrassTile extends TerrainTile {
  private final Color COLOR = generateColor();
  private static final float HEIGHT = 0.3f;

  public GrassTile(Tile tile, Loader loader) {
    super(tile, loader);
    super.bindModel(loader, COLOR);
  }

  private Color generateColor() {
    float variation = (float) (Math.random() * 0.1f - 0.05f);
    Vector3f base = new Vector3f(0.2f, 0.7f, 0.2f);
    return new Color(base.add(variation, variation, variation));
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

    builder.addVertex(new Vector3f(0, HEIGHT, 0));
    builder.addVertex(new Vector3f(0, HEIGHT, 1));
    builder.addVertex(new Vector3f(1, HEIGHT, 1));
    builder.addVertex(new Vector3f(1, HEIGHT, 0));

    builder.generateQuad(0, 1, 2, 3, Maths.UP, COLOR);
  }
}
