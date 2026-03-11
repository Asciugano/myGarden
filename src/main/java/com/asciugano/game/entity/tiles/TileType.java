package com.asciugano.game.entity.tiles;

public enum TileType {
    PATH_TYPE("gray.png"),
    DIRT_TYPE("green.png"),
    GRASS_TYPE("green.png"),
    WATER_TYPE("green.png"),
    SOIL_TYPE("green.png");

    private final String textureName;

    TileType(String textureName) {
        this.textureName = textureName;
    }

    public String getTextureName() { return textureName; }
}
