package com.asciugano.engine.entities;

import com.asciugano.engine.components.RenderComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.models.TexturedModel;
import org.joml.Vector3f;

public class Player extends Entity {
    public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        addComponent(new TransformationComponent(position, rotation, scale));
        addComponent(new RenderComponent(model));
    }
}
