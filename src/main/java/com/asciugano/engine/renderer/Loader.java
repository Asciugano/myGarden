package com.asciugano.engine.renderer;

import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.textures.TextureData;
import com.asciugano.engine.utils.Color;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader {
    private static final String TEXTURE_PATH = "src/main/resources/textures/";

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] position, float[] attribute, int attributeSize, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, position);
        storeDataInAttributeList(1, attributeSize, attribute);
        storeDataInAttributeList(2, 3, normals);

        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    private int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);

        glBindVertexArray(vaoID);

        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int attribSize, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(attributeNumber, attribSize, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int loadTexture(String fileName) {
        int width, height;
        ByteBuffer image;

        try {
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);

            image = STBImage.stbi_load(TEXTURE_PATH + fileName, w, h, comp, 4);

            if (image == null) {
                System.out.println("Failed to load texture: "+ TEXTURE_PATH + fileName);
                throw new RuntimeException("Failed to load texture: " + TEXTURE_PATH + fileName);
            }

            width = w.get();
            height = h.get();
        } catch(Exception e) {
            throw new RuntimeException("Failed to load texture: " + fileName, e);
        }
        int textureID = glGenTextures();
        textures.add(textureID);

        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.5f);

        STBImage.stbi_image_free(image);

        return textureID;
    }

    public RawModel loadToVAO(float[] position, int dimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, position);

        unbindVAO();

        return new RawModel(vaoID, position.length / dimensions);
    }

    public int loadCubeMap(String[] textureFiles) {
        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

        for(int i  = 0; i < textureFiles.length; i++) {
            TextureData data = decodeTextureFile(TEXTURE_PATH + "/skyBox/" + textureFiles[i] + ".png");
            glTexImage2D(
                    GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0,
                    GL_RGBA,
                    data.getWidth(),
                    data.getHeight(),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    data.getBuffer()
            );
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        textures.add(texID);

        return texID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width;
        int height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);

            buffer = STBImage.stbi_load(fileName, w, h, comp, 4);

            if(buffer == null) {
                System.out.println("Failed to load texture: " + fileName + STBImage.stbi_failure_reason());
                System.exit(-1);
            }

            width = w.get();
            height = h.get();
        }

        return new TextureData(buffer, width, height);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices).flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    public void cleanUp() {
        for(int vao : vaos) glDeleteVertexArrays(vao);
        for(int vbo : vbos) glDeleteBuffers(vbo);
        for(int textures : textures) glDeleteTextures(textures);
    }
}
