package com.asciugano.engine.utils;

import org.joml.Vector3f;

public enum Direction {
  NORTH(new Vector3f(0, 1, 0)),
  SUD(new Vector3f(0, -1, 0)),
  EAST(new Vector3f(1, 0, 0)),
  WEST(new Vector3f(-1, 0, 0));

  private Vector3f direction;

  Direction(Vector3f direction) {
    this.direction = direction;
  }

  public Vector3f getVector() {
    return direction;
  }

  public Direction opposite() {
    return switch (this) {
      case NORTH -> SUD;
      case SUD -> NORTH;
      case EAST -> WEST;
      case WEST -> EAST;

      default -> NORTH;
    };
  }
}
