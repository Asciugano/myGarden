package com.asciugano.game.entity.tiles;

import org.joml.Vector3f;

import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.models.QuadBuilder;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.utils.Color;
import com.asciugano.engine.utils.Maths;

public class SoilTile extends TerrainTile {

  private static final Color COLOR = new Color(new Vector3f(0.4f, 0.25f, 0.1f));
  private static final float HEIGHT = -0.2f;

  public SoilTile(Tile tile, Loader loader) {
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

    builder.addVertex(new Vector3f(0, HEIGHT, 0));
    builder.addVertex(new Vector3f(0, HEIGHT, 1));
    builder.addVertex(new Vector3f(1, HEIGHT, 1));
    builder.addVertex(new Vector3f(1, HEIGHT, 0));

    builder.generateQuad(0, 1, 2, 3, Maths.UP, COLOR);
  }
}
