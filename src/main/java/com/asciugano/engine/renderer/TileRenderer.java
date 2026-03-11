package com.asciugano.engine.renderer;

import com.asciugano.engine.components.OffsetComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.utils.Maths;
import com.asciugano.game.entity.tiles.Tile;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TileRenderer {
    private StaticShader shader;

    public TileRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Tile>> tiles) {
        for(TexturedModel model : tiles.keySet()) {
            prepareTexturedModel(model);
            List<Tile> batch = tiles.get(model);

            for(Tile tile : batch) {
                prepareInstance(tile);

                glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shader.loadShineVariables(texturedModel.getTexture().getShineDamper(), texturedModel.getTexture().getReflectivity());

        shader.connectTextureUnits();
        shader.loadNumberOfRows(texturedModel.getTexture().getNumberOfRows());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareInstance(Tile tile) {
        TransformationComponent tc = tile.getComponent(TransformationComponent.class);
        if(tc != null) {
            shader.loadTransformationMatrix(
                    Maths.createTransformationMatrix(
                            tc.getPosition(),
                            tc.getRotation(),
                            tc.getScale()
                    )
            );
        }

        OffsetComponent oc = tile.getComponent(OffsetComponent.class);
        if(oc != null) shader.loadOffset(oc.getOffset());
    }

    public void clenUp() {
        shader.cleanUp();
    }
}
