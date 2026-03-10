package com.asciugano.engine.components;

import org.joml.Vector2f;

public class OffsetComponent implements Component {
    Vector2f offset;

    public OffsetComponent(Vector2f offset) {
        this.offset = offset;
    }

    public Vector2f getOffset() { return offset; }
}
