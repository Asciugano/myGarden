package com.asciugano.engine.display;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.asciugano.engine.entities.Camera;
import com.asciugano.engine.entities.Entity;
import com.asciugano.engine.entities.Light;
import com.asciugano.engine.models.TexturedModel;
import com.asciugano.engine.renderer.Loader;
import com.asciugano.engine.renderer.MasterRenderer;
import com.asciugano.engine.renderer.OBJLoader;
import com.asciugano.engine.terrains.Terrain;
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
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        Loader loader = new Loader();
        MasterRenderer masterRenderer = new MasterRenderer();


        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

        TexturedModel tree = new TexturedModel(OBJLoader.loadOBJModel("tree", loader), new ModelTexture(loader.loadTexture("tree.png")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture.png")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), new ModelTexture(loader.loadTexture("fern.png")));
        fern.getTexture().setHasTransparency(true);
        TexturedModel stall = new TexturedModel(OBJLoader.loadOBJModel("stall", loader), new ModelTexture(loader.loadTexture("stallTexture.png")));
        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass.png")));
        Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass.png")));

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 500; i++) {
            entities.add(new Entity(tree, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), new Vector3f(0, 0, 0), 3));
            entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), new Vector3f(0, 0, 0), 1));
            entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), new Vector3f(0, 0, 0), 0.6f));
        }

        while ( !glfwWindowShouldClose(window) ) {
//            entity.increasePosition(new Vector3f(0, 0, -0.02f));
//            entity.increaseRotation(new Vector3f(0, 1, 0));

//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            masterRenderer.processTerrains(terrain);
            masterRenderer.processTerrains(terrain2);
            for(Entity entity : entities) {
                masterRenderer.processEntity(entity);
            }

            masterRenderer.render(light, camera);
            camera.move();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
    }
}