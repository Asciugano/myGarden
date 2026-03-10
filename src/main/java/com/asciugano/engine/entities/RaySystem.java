package com.asciugano.engine.entities;

import com.asciugano.engine.components.RayComponent;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.entity.Ray;
import org.joml.Vector3f;

public class RaySystem {
    public static void update(Ray ray, Terrain terrain) {
        RayComponent rayComponent = ray.getComponent(RayComponent.class);

        float t = -rayComponent.getOrigin().y / rayComponent.getDirection().y;
        rayComponent.setHitPoint(
                new Vector3f(
                        rayComponent.getOrigin().x + rayComponent.getDirection().x * t,
                        0,
                        rayComponent.getOrigin().z + rayComponent.getDirection().z * t
                )
        );
    }
}
