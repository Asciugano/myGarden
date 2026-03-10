package com.asciugano.engine.skyBox;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.renderer.Loader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SkyBoxRenderer {
    private static final float SIZE = 500;
    private static final float[] VERTICES = {
            -SIZE, SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,


            -SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, -SIZE, SIZE,


            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,


            -SIZE, -SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, SIZE,


            -SIZE, SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, SIZE, -SIZE,


            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, SIZE
    };

    private static final String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
    private static final String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
    private RawModel cube;
    private int texture;
    private int nightTexture;
    private SkyBoxShader shader;
    private float time = 0;

    public SkyBoxRenderer(Loader loader, Matrix4f projection) {
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
        shader = new SkyBoxShader();

        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projection);
        shader.stop();
    }

    public void render(Camera camera, Vector3f fogColor) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(fogColor.x, fogColor.y, fogColor.z);
        glBindVertexArray(cube.getVaoID());
        glEnableVertexAttribArray(0);

        bindTextures();

        glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shader.stop();
    }

    private void bindTextures() {
        time += DisplayManager.getDelta() * 1000;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if (time >= 0 && time < 5000) {
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = (time - 0) / (5000 - 0);
        } else if (time >= 5000 && time < 8000) {
            texture1 = nightTexture;
            texture2 = texture;
            blendFactor = (time - 5000) / (8000 - 5000);
        } else if (time >= 8000 && time < 21000) {
            texture1 = texture;
            texture2 = texture;
            blendFactor = (time - 8000) / (21000 - 8000);
        } else {
            texture1 = texture;
            texture2 = nightTexture;
            blendFactor = (time - 21000) / (24000 - 21000);
        }


        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}