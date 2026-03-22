package com.asciugano.game.entity.tiles;

import com.asciugano.engine.models.ColoredModel;
import com.asciugano.engine.models.MeshBuilder;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.terrains.EdgeVertexGenerator;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.engine.utils.Color;

public abstract class TerrainTile extends Tile {
  protected ColoredModel model;
  protected ColoredModel edgeModel;

  public TerrainTile(Tile tile, Loader loader) {
    super(tile.getGridX(), tile.getGridZ(), tile.getChunkX(), tile.getChunkZ());
  }

  protected void bindModel(Loader loader, Color color) {
    MeshBuilder meshBuilder = new MeshBuilder();

    generateMeshVertices(meshBuilder);
    this.model = new ColoredModel(
        loader.loadToVAO(
            meshBuilder.getVertices(),
            meshBuilder.getColors(),
            3,
            meshBuilder.getNormals(),
            meshBuilder.getIndices()),
        color);
  }

  public ColoredModel getModel() {
    return model;
  }

  public void fillGaps(Loader loader, MeshBuilder builder, Color color) {
    EdgeVertexGenerator.generateEdgeVertices(this, builder, Terrain.getChunk(chunkX, chunkZ));
    this.edgeModel = new ColoredModel(
        loader.loadToVAO(
            builder.getVertices(),
            builder.getColors(),
            3,
            builder.getNormals(),
            builder.getIndices()),
        color);
  }

  public abstract float getEdgeHeight();

  public abstract Color getEdgeColor();

  protected abstract void generateMeshVertices(MeshBuilder meshBuilder);
}
