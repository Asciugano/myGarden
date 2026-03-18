package com.asciugano.engine.models;

import com.asciugano.engine.utils.Color;
import org.joml.Vector3f;

public class QuadBuilder {
  private final MeshBuilder meshBuilder;

  public QuadBuilder(MeshBuilder meshBuilder) {
    this.meshBuilder = meshBuilder;
  }

  public void addVertex(Vector3f pos) {
    meshBuilder.addVertex(pos);
  }

  public void generateQuad(int v0, int v1, int v2, int v3, Vector3f normal, Color color) {
    meshBuilder.addQuad(v0, v1, v2, v3, normal, color);
  }

  public void generateQuad(int v0, int v1, int v2, int v3, Color color) {
    meshBuilder.addQuad(v0, v1, v2, v3, color);
  }

  public void generateQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Color color) {
    meshBuilder.addQuad(v0, v1, v2, v3, color);
  }
}
