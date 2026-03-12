package com.asciugano.engine.UIManager;

import com.asciugano.engine.utils.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class UIEntity extends ComponentUser {
    protected Vector3f color;
    protected UITexture ui;
    protected Vector2f scale;
    protected Vector2f position;

    public UIEntity(UITexture ui, Vector2f scale,  Vector2f position) {
        this.ui = ui;
        this.color = Color.WHITE;
        this.scale = scale;
        this.position = position;

        addComponent(new UIComponent(ui, color));
    }
    public UIEntity(UITexture ui, Vector3f color, Vector2f scale) {
        this.ui = ui;
        this.color = color;
        this.scale = scale;
        position = new Vector2f(0, 0);

        addComponent(new UIComponent(ui, color));
    }

    public void update(float dt) { }

    public UITexture getUI() { return ui; }
    public Vector3f getColor() { return color; }

    public Vector2f getScale() { return scale; }
    public Vector2f getPosition() { return position; }
}
