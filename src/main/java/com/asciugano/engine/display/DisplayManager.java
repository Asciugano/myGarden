package com.asciugano.engine.display;

import com.asciugano.engine.entities.Camera;
//import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.handlers.mouse.MousePicker;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.handlers.KeyHandler;
import com.asciugano.engine.handlers.mouse.MouseHandler;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.renderer.MasterRenderer;
import com.asciugano.engine.terrains.Terrain;
import com.asciugano.game.entity.tiles.Tile;
import com.asciugano.game.scene.Scene;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {
    private static long window;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static float lastFrameTime;
    private static float delta;

    private float lastX, lastY;

    public static int getWidth() { return WIDTH; }
    public static int getHeight() { return HEIGHT; }
    public static long getWindow() { return window; }

    public long getExternalMonitor() {
        PointerBuffer monitors = glfwGetMonitors();
        if(monitors == null || monitors.capacity() < 2) {
            System.out.println("No external monitors found");
            return glfwGetPrimaryMonitor();
        }

        return monitors.get(1);
    }

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.out).set();

        if(!glfwInit()) {
            System.out.println("Unable to initialize GLFW");
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "My Garden", NULL, NULL);

        if(window == NULL) {
            System.out.println("Failed to create the GLFW window");
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);

            switch (key) {
                case GLFW_KEY_SPACE -> {
                    if(action == GLFW_RELEASE) KeyHandler.SPACE_PRESSED = false;
                    else if(action == GLFW_PRESS) KeyHandler.SPACE_PRESSED = true;
                }
                case GLFW_KEY_A -> {
                    if(action == GLFW_RELEASE) KeyHandler.A_PRESSED = false;
                    else if(action == GLFW_PRESS) KeyHandler.A_PRESSED = true;
                }
                case GLFW_KEY_D -> {
                    if(action == GLFW_RELEASE) KeyHandler.D_PRESSED = false;
                    else if(action == GLFW_PRESS) KeyHandler.D_PRESSED = true;
                }
                case GLFW_KEY_W -> {
                    if(action == GLFW_RELEASE) KeyHandler.W_PRESSED = false;
                    else if(action == GLFW_PRESS) KeyHandler.W_PRESSED = true;
                }
                case GLFW_KEY_S -> {
                    if(action == GLFW_RELEASE) KeyHandler.S_PRESSED = false;
                    else if(action == GLFW_PRESS) KeyHandler.S_PRESSED = true;
                }
            }
        });

        glfwSetCursorPosCallback(window, (windowH, x, y) -> {
            MouseHandler.mouseX = (float) x;
            MouseHandler.mouseY = (float) y;
            float dx = (float)(x - lastX);
            float dy = (float)(y - lastY);

            MouseHandler.dx = dx;
            MouseHandler.dy = dy;

            lastX = (float) x;
            lastY = (float) y;
            if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
                MouseHandler.LEFT_PRESSED = true;
            } else if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) {
                MouseHandler.LEFT_PRESSED = false;
            }
            if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS) {
                MouseHandler.RIGHT_PRESSED = true;
            } else if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_RELEASE) {
                MouseHandler.RIGHT_PRESSED = false;
            }
            if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_MIDDLE) == GLFW_PRESS) {
                MouseHandler.WHEEL_PRESSED = true;
            } else if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_MIDDLE) == GLFW_RELEASE) {
                MouseHandler.WHEEL_PRESSED = false;
            }
        });
        glfwSetScrollCallback(window, (windowH, x, y) -> {
            MouseHandler.scroll = (float) y;
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(getExternalMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth .get(0)) / 2,
                    (vidmode.height() - pHeight .get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0, 0, WIDTH, HEIGHT);

        System.out.println(glGetString(GL_VERSION));

        glfwSwapInterval(1);

        glfwShowWindow(window);
        lastFrameTime = getCurrentTime();
    }

    private void loop() {
        glEnable(GL_DEPTH_TEST);
        glClearColor(1, 1, 1, 1);

        Loader loader = new Loader();
        Scene scene = new Scene(loader);

        while(!glfwWindowShouldClose(window)) {
            MouseHandler.reset();
            glfwPollEvents();

            float currentFrameTime = getCurrentTime();
            delta = (currentFrameTime - lastFrameTime) / 1000;

            scene.update(delta);
            scene.render();

            glfwSwapBuffers(window);
        }

        scene.cleanUp();
    }

    private float getCurrentTime() {
        return (float) glfwGetTime() * 1000;
    }

    public static float getDelta() { return delta; }
}
