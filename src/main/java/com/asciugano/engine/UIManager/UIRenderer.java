package com.asciugano.engine.UIManager;

import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.utils.Maths;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class UIRenderer {

    private final RawModel quad;
    private UIShader shader;

    public UIRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new UIShader();
    }

    public void render(List<UITexture> uis) {
        glBindVertexArray(quad.getVaoID());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        shader.start();

        for (UITexture ui : uis) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, ui.getTexture());

            shader.loadTransformationMatrix(Maths.createTransformationMatrix(ui.getPosition(), ui.getScale()));

            glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }


        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}