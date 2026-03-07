package com.asciugano.engine.entities;

import com.asciugano.engine.display.DisplayManager;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private Vector3f position = new Vector3f(0, 5, 0);
    private float pitch;
    private float yaw = 180;
    private float roll;

    public void move() {

        long window = DisplayManager.getWindow();

        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                position.y -= 0.1f;
            } else {
                position.z += 0.1f;
            }
        }

        if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                position.y += 0.1f;
            } else {
                position.z -= 0.1f;
            }
        }

        if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS){
            position.x += 0.1f;
        }

        if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS){
            position.x -= 0.1f;
        }
    }
    public Vector3f getPosition() { return position; }

    public float getPitch() { return pitch; }

    public float getYaw() { return yaw; }

    public float getRoll() { return roll; }
}
