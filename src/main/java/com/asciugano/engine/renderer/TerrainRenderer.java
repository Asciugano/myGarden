package com.asciugano.engine.renderer;

import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.shaders.TerrainShader;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.engine.textures.TerrainTexturePack;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);

            glDrawElements(GL_TRIANGLES, terrain.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);

            unbindTexturedModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        RawModel model = terrain.getModel();

        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shader.loadShineVariables(
                1,
                0
        );

        bindTextures(terrain);
        shader.connectTextureUnits();
    }

//    private void bindTextures(Terrain terrain) {
//        TerrainTexturePack texturePack = terrain.getTexturePack();
//
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
//        glActiveTexture(GL_TEXTURE1);
//        glBindTexture(GL_TEXTURE_2D, texturePack.getRTexture().getTextureID());
//        glActiveTexture(GL_TEXTURE2);
//        glBindTexture(GL_TEXTURE_2D, texturePack.getGTexture().getTextureID());
//        glActiveTexture(GL_TEXTURE3);
//        glBindTexture(GL_TEXTURE_2D, texturePack.getBTexture().getTextureID());
//        glActiveTexture(GL_TEXTURE4);
//        glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
//    }
    private void bindTextures(Terrain terrain) {
        glActiveTexture(GL_TEXTURE0 );
        glBindTexture(GL_TEXTURE_2D, terrain.getTexture());
    }

    private void unbindTexturedModel() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()),
                new Vector3f(0, 0, 0),
                1
        );
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
