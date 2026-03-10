package com.asciugano.game.entity;

import com.asciugano.engine.components.RayComponent;
import com.asciugano.engine.components.TransformationComponent;
import com.asciugano.engine.entities.Camera;
import org.joml.Vector3f;

public class Ray extends Entity {
    public Ray(Vector3f origin, Camera camera) {
        addComponent(new RayComponent(origin, new Vector3f(0, 0, 1)));
    }

    private void update(Vector3f newOrigin, Vector3f newDirection) {
        RayComponent ray = getComponent(RayComponent.class);
        ray.setOrigin(newOrigin);
        ray.setDirection(newDirection.normalize());
    }
}
