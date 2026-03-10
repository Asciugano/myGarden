package com.asciugano.engine.components;

import com.asciugano.engine.entities.Camera;
import com.asciugano.game.entity.Entity;
import com.asciugano.game.entity.Ray;

public class PointerComponent implements Component {
    private Entity target;
    private Ray ray;

    public PointerComponent(Camera camera) {
//        ray = new Ray(camera);
    }

    public Ray getRay() { return ray; }
    public Entity getTarget() { return target; }

    public void setTarget(Entity target) { this.target = target; }
}
