package com.asciugano.engine.renderer;

import com.asciugano.engine.models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader {

    private static final String TEXTURE_PATH = "src/main/resources/textures/";

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] position, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, position);
        storeDataInAttributeList(1, 2, textureCoords);
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
        MemoryUtil.memFree(buffer);
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
                System.out.println("Failed to load texture: " + fileName);
                throw new RuntimeException("Failed to load texture: " + fileName);
            }

            width = w.get();
            height = h.get();

            MemoryUtil.memFree(w);
            MemoryUtil.memFree(h);
            MemoryUtil.memFree(comp);
        } catch(Exception e) {
            throw new RuntimeException("Failed to load texture: " + fileName, e);
        }
            int textureID = glGenTextures();
            textures.add(textureID);

            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(image);

            return textureID;
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices).flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        MemoryUtil.memFree(buffer);
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
