package com.asciugano.engine.models;

import com.asciugano.engine.utils.Color;
import com.asciugano.engine.utils.Maths;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder {
  private final List<Float> vertices = new ArrayList<>();
  private final List<Float> normals = new ArrayList<>();
  private final List<Float> colors = new ArrayList<>();
  private final List<Integer> indices = new ArrayList<>();

  public void addVertex(Vector3f pos) {
    vertices.add(pos.x);
    vertices.add(pos.y);
    vertices.add(pos.z);
  }

  public void addVertex(Vector3f pos, Vector3f normal, Color color) {
    vertices.add(pos.x);
    vertices.add(pos.y);
    vertices.add(pos.z);

    normals.add(normal.x);
    normals.add(normal.y);
    normals.add(normal.z);

    colors.add(color.color.x);
    colors.add(color.color.y);
    colors.add(color.color.z);
  }

  public void addQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Color color) {
    Vector3f normal = calculateNormal(v0, v1, v2);

    int startIndex = vertices.size() / 3;
    addVertex(v0, normal, color);
    addVertex(v1, normal, color);
    addVertex(v2, normal, color);
    addVertex(v3, normal, color);

    indices.add(startIndex);
    indices.add(startIndex + 1);
    indices.add(startIndex + 2);

    indices.add(startIndex + 2);
    indices.add(startIndex + 3);
    indices.add(startIndex);
  }

  public void addQuad(int v0, int v1, int v2, int v3, Color color) {
    indices.add(v0);
    indices.add(v1);
    indices.add(v2);
    indices.add(v2);
    indices.add(v3);
    indices.add(v0);

    for (int i = 0; i < 4; i++) {
      normals.add(Maths.UP.x);
      normals.add(Maths.UP.y);
      normals.add(Maths.UP.z);

      colors.add(color.color.x);
      colors.add(color.color.y);
      colors.add(color.color.z);
    }
  }

  public void addQuad(int v0, int v1, int v2, int v3, Vector3f n, Color color) {
    indices.add(v0);
    indices.add(v1);
    indices.add(v2);
    indices.add(v2);
    indices.add(v3);
    indices.add(v0);

    for (int i = 0; i < 4; i++) {
      normals.add(n.x);
      normals.add(n.y);
      normals.add(n.z);

      colors.add(color.color.x);
      colors.add(color.color.y);
      colors.add(color.color.z);
    }
  }

  private Vector3f calculateNormal(Vector3f v0, Vector3f v1, Vector3f v2) {
    Vector3f edge1 = new Vector3f();
    Vector3f edge2 = new Vector3f();

    v1.sub(v0, edge1);
    v2.sub(v0, edge2);

    Vector3f normal = new Vector3f();
    edge1.cross(edge2, normal);

    return normal.normalize();
  }

  public int[] getIndices() {
    return indices.stream().mapToInt(Integer::intValue).toArray();
  }

  public float[] getVertices() {
    float[] res = new float[vertices.size()];
    for (int i = 0; i < vertices.size(); i++) {
      res[i] = vertices.get(i);
    }

    return res;
  }

  public float[] getNormals() {
    float[] res = new float[normals.size()];
    for (int i = 0; i < normals.size(); i++) {
      res[i] = normals.get(i);
    }

    return res;
  }

  public float[] getColors() {
    float[] res = new float[colors.size()];
    for (int i = 0; i < colors.size(); i++) {
      res[i] = colors.get(i);
    }

    return res;
  }
}
