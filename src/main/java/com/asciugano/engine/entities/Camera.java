package com.asciugano.engine.entities;

import com.asciugano.engine.display.DisplayManager;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public void move() {
        glfwSetKeyCallback(DisplayManager.getWindow(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_W) {
                position.z += 1f;
            }
            if (key == GLFW_KEY_S) {
                position.z -= 1f;
            }
            if (key == GLFW_KEY_A) {
                position.x += 1f;
            }
            if (key == GLFW_KEY_D) {
                position.x -= 1f;
            }
        });
    }

    public Vector3f getPosition() { return position; }

    public float getPitch() { return pitch; }

    public float getYaw() { return yaw; }

    public float getRoll() { return roll; }
}
