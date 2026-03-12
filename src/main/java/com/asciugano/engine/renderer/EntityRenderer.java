package com.asciugano.engine.renderer;

import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntityRenderer {
    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        for(TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);

            for(Entity entity : batch) {
                if(entity.getComponent(RenderComponent.class) == null) continue;
                prepareInstance(entity);

                glDrawElements(
                        GL_TRIANGLES,
                        model.getRawModel().getVertexCount(),
                        GL_UNSIGNED_INT,
                        0
                );
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel model = texturedModel.getRawModel();

        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shader.loadShineVariables(
                texturedModel.getTexture().getShineDamper(),
                texturedModel.getTexture().getReflectivity()
        );

        if(texturedModel.getTexture().isHasTransparency())
            MasterRenderer.disableCulling();

        shader.loadFakeLighting(texturedModel.getTexture().isUseFakeLighting());

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

    private void prepareInstance(Entity entity) {
        if(entity.getComponent(TransformationComponent.class) != null) {
            Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                    entity.getComponent(TransformationComponent.class).getPosition(),
                    entity.getComponent(TransformationComponent.class).getRotation(),
                    entity.getComponent(TransformationComponent.class).getScale()
            );

            shader.loadTransformationMatrix(transformationMatrix);
        }

        if(entity.getComponent(OffsetComponent.class) != null) {
            shader.loadOffset(
                    entity.getComponent(OffsetComponent.class).getOffset()
            );
        }
    }
}
