package com.asciugano.engine.renderer;

import com.asciugano.engine.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {
    private static final String PATH = "src/main/resources/objects/";

    public static RawModel loadOBJModel(String fileName, Loader loader) {

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray;
        float[] texturesArray;
        float[] normalsArray;
        int[] indicesArray;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(PATH + fileName + ".obj"))) {

            String line;

            // -------- Parse vertex data --------
            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("v ")) {

                    String[] tokens = line.split("\\s+");

                    vertices.add(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));

                } else if (line.startsWith("vt ")) {

                    String[] tokens = line.split("\\s+");

                    textures.add(new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    ));

                } else if (line.startsWith("vn ")) {

                    String[] tokens = line.split("\\s+");

                    normals.add(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));

                } else if (line.startsWith("f ")) {
                    break;
                }
            }

            texturesArray = new float[vertices.size() * 2];
            normalsArray = new float[vertices.size() * 3];

            // -------- Parse face data --------
            do {

                if (line == null || !line.startsWith("f "))
                    continue;

                String[] tokens = line.trim().split("\\s+");

                for (int i = 1; i < 4; i++) {

                    String[] vertexData = tokens[i].split("/");

                    processVertex(
                            vertexData,
                            indices,
                            textures,
                            normals,
                            texturesArray,
                            normalsArray
                    );
                }

            } while ((line = reader.readLine()) != null);

            verticesArray = new float[vertices.size() * 3];

            int pointer = 0;
            for (Vector3f vertex : vertices) {
                verticesArray[pointer++] = vertex.x;
                verticesArray[pointer++] = vertex.y;
                verticesArray[pointer++] = vertex.z;
            }

            indicesArray = indices.stream().mapToInt(i -> i).toArray();

            return loader.loadToVAO(
                    verticesArray,
                    texturesArray,
                    indicesArray
            );

        } catch (Exception e) {
            throw new RuntimeException("Error loading OBJ: " + fileName, e);
        }
    }

    private static void processVertex(
            String[] vertexData,
            List<Integer> indices,
            List<Vector2f> textures,
            List<Vector3f> normals,
            float[] texturesArray,
            float[] normalsArray
    ) {

        int vertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(vertexPointer);

        if (vertexData.length > 1 && !vertexData[1].isEmpty()) {

            Vector2f texture = textures.get(
                    Integer.parseInt(vertexData[1]) - 1
            );

            texturesArray[vertexPointer * 2] = texture.x;
            texturesArray[vertexPointer * 2 + 1] = 1 - texture.y;
        }

        if (vertexData.length > 2 && !vertexData[2].isEmpty()) {

            Vector3f normal = normals.get(
                    Integer.parseInt(vertexData[2]) - 1
            );

            normalsArray[vertexPointer * 3] = normal.x;
            normalsArray[vertexPointer * 3 + 1] = normal.y;
            normalsArray[vertexPointer * 3 + 2] = normal.z;
        }
    }
}
