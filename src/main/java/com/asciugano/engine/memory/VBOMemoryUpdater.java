package com.asciugano.engine.memory;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.asciugano.engine.models.MeshData;
import com.asciugano.game.entity.tiles.chunks.Chunk;

public class VBOMemoryUpdater<T> {
  private static final float RESIZE_FACTOR = 1.2f;
  private ByteBuffer buffer = BufferUtils.createByteBuffer(512 * MeshData.BYTES_PER_VERTEX);// INFO: max 512 vertici
  private final MeshData meshData;
  private final MemoryMapper<T> memoryMapper;

  public VBOMemoryUpdater(MeshData meshData, MemoryMapper<T> memoryMapper) {
    this.meshData = meshData;
    this.memoryMapper = memoryMapper;
  }

  public void store(T objectKey, ByteBuffer vertexData) {
    resizeIfNecessary(vertexData.capacity());
    MemorySlot slot = memoryMapper.store(objectKey, vertexData);
    storeDataInVBO(slot, vertexData);
    updateMeshVertexCount();

    if (objectKey instanceof Chunk chunk) {
      chunk.setRenderOffset(slot.getStartIndex(), slot.getLenght());
    }
  }

  public void remove(T objectKey) {
    MemorySlot slot = memoryMapper.remove(objectKey);
    if (slot == null)
      return;
    if (slot.getStartIndex() >= memoryMapper.getCurrentByteSize())
      updateMeshVertexCount();
    else
      storeNullDataInVBO(slot);
  }

  private void resizeIfNecessary(int newDataLenght) {
    int currentSize = memoryMapper.getCurrentByteSize();
    long vboCapacity = meshData.getVBOCapacity();
    if (currentSize + newDataLenght >= vboCapacity)
      meshData.getMeshDataVBO().resize(currentSize, (long) (vboCapacity * RESIZE_FACTOR));
  }

  private void storeDataInVBO(MemorySlot slot, ByteBuffer data) {
    buffer = storeDataInBuffer(data);
    storeBufferInVBO(buffer, slot.getStartIndex());
  }

  private void storeNullDataInVBO(MemorySlot slot) {
    ByteBuffer buffer = getNullData(slot.getLenght());
    storeBufferInVBO(buffer, slot.getStartIndex());
  }

  private ByteBuffer storeDataInBuffer(ByteBuffer data) {
    data.rewind();
    if (buffer.capacity() < data.capacity())
      buffer = BufferUtils.createByteBuffer(data.capacity());

    buffer.clear();
    buffer.put(data);
    buffer.flip();
    return buffer;
  }

  private ByteBuffer getNullData(int lenght) {
    ByteBuffer nullBuffer = BufferUtils.createByteBuffer(lenght);
    for (int i = 0; i < lenght; i++) {
      nullBuffer.put((byte) 0);
    }
    nullBuffer.flip();
    return nullBuffer;
  }

  private void storeBufferInVBO(ByteBuffer buffer, int offset) {
    meshData.updateData(offset, buffer);
  }

  private void updateMeshVertexCount() {
    int vertexCount = memoryMapper.getCurrentByteSize() / MeshData.BYTES_PER_VERTEX;
    meshData.setVertexCount(vertexCount);
  }

  public MeshData getMeshData() {
    return meshData;
  }
}
