package com.asciugano.engine.models;

import com.asciugano.engine.utils.Color;
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

    public void addQuad(int v0, int v1, int v2, int v3, Color color) {
        indices.add(v0);
        indices.add(v1);
        indices.add(v2);
        indices.add(v2);
        indices.add(v3);
        indices.add(v0);

        colors.add(color.color.x);
        colors.add(color.color.y);
        colors.add(color.color.z);
    }

    public void addQuad(int v0, int v1, int v2, int v3, Vector3f n, Color color) {
        indices.add(v0);
        indices.add(v1);
        indices.add(v2);
        indices.add(v2);
        indices.add(v3);
        indices.add(v0);

        for(int i = 0; i < 4; i++) {
            normals.add(n.x);
            normals.add(n.y);
            normals.add(n.z);

            colors.add(color.color.x);
            colors.add(color.color.y);
            colors.add(color.color.z);
        }
    }

    public int[] getIndices() { return indices.stream().mapToInt(Integer::intValue).toArray(); }
    public float[] getVertices() {
        float[] res =  new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            res[i] = vertices.get(i);
        }

        return res;
    }

    public float[] getNormals() {
        float[] res =  new float[normals.size()];
        for (int i = 0; i < normals.size(); i++) {
            res[i] = normals.get(i);
        }

        return res;
    }

    public float[] getColors() {
        float[] res =  new float[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            res[i] = colors.get(i);
        }

        return res;
    }
}
