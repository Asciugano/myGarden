package com.asciugano.engine.terrains;

import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.textures.TerrainTexture;
import com.asciugano.engine.textures.TerrainTexturePack;
import com.asciugano.engine.utils.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class Terrain {
    private static final float SIZE = 800;
    private static final float VERTEX_COUNT = 128;

    public static float getSize() { return SIZE; }

    private float x, z;
    private RawModel model;
    private int texture;
//    private TerrainTexturePack texturePack;
//    private TerrainTexture blendMap;

    public Terrain(
            int gridX,
            int gridZ,
            Loader loader
//            TerrainTexturePack texturePack,
//            TerrainTexture blendMap,
//            String heightMap
    ) {
//        this.texturePack = texturePack;
//        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.texture = loader.loadTexture("green.png");

        model = generateTerrain(loader);

//        this.model = generateTerrain(loader, heightMap);
    }

    public float getX() { return x; }

    public float getZ() { return z; }

    public RawModel getModel() { return model; }

//    public TerrainTexturePack getTexturePack() { return texturePack; }
//
//    public TerrainTexture getBlendMap() { return blendMap; }

    private RawModel generateTerrain(Loader loader) {
        int count = (int) (VERTEX_COUNT * VERTEX_COUNT);
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[(int) (6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1))];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/(VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = 0;
                vertices[vertexPointer*3+2] = (float)i/(VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
                textureCoords[vertexPointer*2] = (float)j/(VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/(VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (int) ((gz*VERTEX_COUNT)+gx);
                int topRight = topLeft + 1;
                int bottomLeft = (int) (((gz+1)*VERTEX_COUNT)+gx);
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    public int getTexture() { return texture; }
}
