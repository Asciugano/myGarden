package com.asciugano.engine.renderer;

import com.asciugano.engine.display.DisplayManager;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.utils.Maths;
import org.joml.Matrix4f;

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

    public Renderer(StaticShader shader) {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Entity entity, StaticShader shader) {
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.getRawModel();

        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotation(),
                entity.getScale()
        );
        shader.loadTransformationMatrix(transformationMatrix);

        shader.connectTextureUnits();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());

        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();

//        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
//        float x_scale = y_scale / aspectRatio;
//        float frustum_length = FAR_PLANE - NEAR_PLANE;
//
//        projectionMatrix = new Matrix4f();
//        projectionMatrix.m00(x_scale);
//        projectionMatrix.m11(y_scale);
//        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
//        projectionMatrix.m23(-1);
//        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
//        projectionMatrix.m33(0);

        projectionMatrix = new Matrix4f().perspective(
                FOV,
                aspectRatio,
                NEAR_PLANE,
                FAR_PLANE
        );
    }
}
