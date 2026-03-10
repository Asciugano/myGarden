package com.asciugano.engine.components;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.handlers.mouse.MousePicker;
import com.asciugano.game.entity.Ray;
import org.joml.Vector3f;

public class PointerComponent {
    private Vector3f target = new Vector3f();
    private Ray ray;

    public PointerComponent(Vector3f origin, Camera camera) {
        ray = new Ray(origin, camera);
    }

    public Ray getRay() { return ray; }
    public Vector3f getTarget() { return target; }
}
