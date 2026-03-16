package com.asciugano.game.entity.tiles;

import com.asciugano.engine.models.ColoredModel;
import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.utils.Color;
import org.joml.Vector3f;

public abstract class TerrainTile extends Tile {
  protected ColoredModel model;

  public TerrainTile(Tile tile, Loader loader) {
    super(tile.getGridX(), tile.getGridZ());
    MeshBuilder meshBuilder = new MeshBuilder();

    generateMeshVertices(meshBuilder);
    this.model = new ColoredModel(
        loader.loadToVAO(
            meshBuilder.getVertices(),
            meshBuilder.getColors(),
            3,
            meshBuilder.getNormals(),
            meshBuilder.getIndices()),
        new Color(new Vector3f(meshBuilder.getColors())));
  }

  public ColoredModel getModel() {
    return model;
  }

  public abstract float getEdgeHeight();

  public abstract Color getEdgeColor();

  protected abstract void generateMeshVertices(MeshBuilder meshBuilder);
}
