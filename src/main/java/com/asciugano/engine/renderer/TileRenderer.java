package com.asciugano.engine.renderer;

import com.asciugano.engine.components.EntityTransform;
import com.asciugano.engine.models.ColoredModel;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.TileShader;
import com.asciugano.engine.utils.Maths;
import com.asciugano.game.entity.tiles.Tile;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TileRenderer {
    private TileShader shader;

    public TileRenderer(TileShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<ColoredModel, List<Tile>> tiles) {
        for(ColoredModel model : tiles.keySet()) {
            prepareTexturedModel(model);
            List<Tile> batch = tiles.get(model);

            for(Tile tile : batch) {
                prepareInstance(tile);

                glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }

            unbindModel();
        }
    }

    private void prepareTexturedModel(ColoredModel coloredModel) {
        RawModel rawModel = coloredModel.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    private void unbindModel() {
        MasterRenderer.enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void prepareInstance(Tile tile) {
            shader.loadTransformationMatrix(
                    Maths.createTransformationMatrix(
                            tile.getWorldPos(),
                            Maths.ZERO_ROT,
                            Tile.getTileSize()
                    )
            );
    }
}
