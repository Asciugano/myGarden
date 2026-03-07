package com.asciugano.engine.renderer;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;

    private Matrix4f projectionMatrix;

    private StaticShader shader;

    public Renderer(StaticShader shader) {
        this.shader = shader;
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BACK);
        for(TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity : batch) {
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

        shader.connectTextureUnits();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotation(),
                entity.getScale()
        );
        shader.loadTransformationMatrix(transformationMatrix);
    }

//    public void render(Entity entity, StaticShader shader) {
////        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
//        glEnable(GL_CULL_FACE);
//        glEnable(GL_BACK);
//        TexturedModel texturedModel = entity.getModel();
//        RawModel model = texturedModel.getRawModel();
//
//        glBindVertexArray(model.getVaoID());
//        glEnableVertexAttribArray(0);
//        glEnableVertexAttribArray(1);
//        glEnableVertexAttribArray(2);
//
//        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
//                entity.getPosition(),
//                entity.getRotation(),
//                entity.getScale()
//        );
//        shader.loadTransformationMatrix(transformationMatrix);
//
//        shader.loadShineVariables(
//                texturedModel.getTexture().getShineDamper(),
//                texturedModel.getTexture().getReflectivity()
//        );
//
//        shader.connectTextureUnits();
//
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
//
//        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
//
//        glDisableVertexAttribArray(0);
//        glDisableVertexAttribArray(1);
//        glDisableVertexAttribArray(2);
//
//        glBindVertexArray(0);
//    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();

        projectionMatrix = new Matrix4f().perspective(
                FOV,
                aspectRatio,
                NEAR_PLANE,
                FAR_PLANE
        );
    }
}
