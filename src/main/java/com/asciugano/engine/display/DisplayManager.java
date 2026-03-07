package com.asciugano.engine.display;

import java.nio.IntBuffer;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.models.RawModel;
import com.asciugano.engine.renderer.MasterRenderer;
import com.asciugano.engine.renderer.OBJLoader;
import com.asciugano.engine.renderer.Renderer;
import com.asciugano.engine.shaders.StaticShader;
import com.asciugano.engine.textures.ModelTexture;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {
    private static long window;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public static int getWidth() { return WIDTH; }
    public static int getHeight() { return HEIGHT; }
    public static long getWindow() { return window; }

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }


    private long getExternalMonitor() {
        PointerBuffer monitors = glfwGetMonitors();

        if(monitors == null || monitors.capacity() < 2) {
            System.out.println("No external monitors found");
            return glfwGetPrimaryMonitor();
        }

        return monitors.get(1);
    }
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "My Garden", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
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
    }

    private void loop() {
        glEnable(GL_DEPTH_TEST);
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        Loader loader = new Loader();
        MasterRenderer masterRenderer = new MasterRenderer();


        RawModel model = OBJLoader.loadOBJModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("white.png"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(texturedModel, new Vector3f(0, -2, -25), new Vector3f(0, 0, 0), 1);

        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

        while ( !glfwWindowShouldClose(window) ) {
//            entity.increasePosition(new Vector3f(0, 0, -0.02f));
//            entity.increaseRotation(new Vector3f(0, 1, 0));

//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            masterRenderer.processEntities(entity);

            masterRenderer.render(light, camera);
            camera.move();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
    }
}