package com.asciugano.engine.models;

import java.nio.ByteBuffer;

public class MeshData {

  public static final int BYTES_PER_VERTEX = 36; // INFO: si puo' cambiare

  private final MeshDataVBO meshDataVBO;
  private int vertexCount;

  public MeshData(long initialCapacity) {
    this.meshDataVBO = new MeshDataVBO(initialCapacity);
  }

  public MeshDataVBO getMeshDataVBO() {
    return meshDataVBO;
  }

  public long getVBOCapacity() {
    return meshDataVBO.getCapacity();
  }

  public void updateData(long offset, ByteBuffer buffer) {
    meshDataVBO.updateData(offset, buffer);
  }

  public void setVertexCount(int count) {
    this.vertexCount = count;
  }

  public int getVertexCount() {
    return this.vertexCount;
  }
}
