package com.asciugano.game.entity.tiles.components;

import com.asciugano.engine.components.Component;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.game.entity.tiles.TileType;

public class TileComponent implements Component {
    private TileType tileType;
    private boolean canModify;
    private TexturedModel model;

    public TileComponent(TileType tileType, boolean canModify, TexturedModel model) {
        this.tileType = tileType;
        this.canModify = canModify;
        this.model = model;
    }

    public TileType getTileType() { return tileType; }

    public void setTileType(TileType tileType) { this.tileType = tileType; }

    public boolean isCanModify() { return canModify; }

    public void setCanModify(boolean canModify) { this.canModify = canModify; }

    public void setModel(TexturedModel model) { this.model = model; }
    public TexturedModel getModel() { return model; }
}
