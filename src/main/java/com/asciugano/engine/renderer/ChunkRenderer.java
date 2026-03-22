package com.asciugano.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.shaders.TileShader;
import com.asciugano.engine.utils.Maths;
import com.asciugano.game.entity.tiles.chunks.Chunk;

public class ChunkRenderer {

  private TileShader shader;

  public ChunkRenderer(TileShader shader, Matrix4f projectionMatrix) {
    this.shader = shader;
    this.shader.start();
    this.shader.loadProjectionMatrix(projectionMatrix);
    this.shader.stop();
  }

  public void render(List<Chunk> chunks) {
    for (Chunk chunk : chunks) {
      RawModel model = chunk.getModel();
      prepareModel(model);
      prepareInstance(chunk);

      Vector3f pos = chunk.getWorldPos();
      System.out.println("chunk x: " + pos.x + " chunk z: " + pos.z);
      glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

      unbindModel(model);
    }
  }

  private void prepareModel(RawModel model) {
    glBindVertexArray(model.getVaoID());

    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glEnableVertexAttribArray(2);
  }

  private void unbindModel(RawModel model) {
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    glDisableVertexAttribArray(2);

    glBindVertexArray(0);
  }

  private void prepareInstance(Chunk chunk) {
    shader.loadTransformationMatrix(
        Maths.createTransformationMatrix(
            chunk.getWorldPos(),
            Maths.ZERO_ROT,
            1));
  }
}
