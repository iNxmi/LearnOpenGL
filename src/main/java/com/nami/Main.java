package com.nami;

import com.nami.camera.Camera;
import com.nami.light.*;
import com.nami.settings.ControlSettings;
import com.nami.settings.Settings;
import com.nami.shader.Shader;
import com.nami.shader.ShaderProgram;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    public final Settings settings = Settings.load("./src/main/java/com/nami/settings/settings.json");
    public Window window;

    public float DELTA_TIME;

    private final float CLEAR_COLOR = 30.0f / 255.0f;

    private final float[] cubeVertices = {
            // positions          // normals           // texture coords
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    private final int vbo, cubeVAO, lightVAO;
    private final Matrix4f[] cubeModels;

    private final Material material;
    private final List<DirectionalLight> directionalLights;
    private final List<PointLight> pointLights;
    private final SpotLight torch;
    private final List<SpotLight> spotLights;

    private final ShaderProgram lightingShader, lightCubeShader;

    private final Camera camera;

    public Main() throws IOException {
        System.out.println(settings);

        window = new Window(settings.window().width(), settings.window().height(), "LWJGL OpenGL");
        window.init(settings.window().fps(), settings.render().msaa(), settings.window().monitor());

        //KeyCallbacks
        glfwSetKeyCallback(window.id(), new GLFWKeyCallback() {
            @Override
            public void invoke(long id, int key, int scancode, int action, int mods) {
                if (action != GLFW_PRESS)
                    return;

                //Exit
                if (key == settings.controls().exit())
                    window.shouldClose(true);

                //Fullscreen
                if (key == settings.controls().fullscreen())
                    window.setFullscreen(glfwGetWindowMonitor(id) == NULL);

                //Screenshot
                if (key == settings.controls().screenshot()) {
                    IntBuffer w = BufferUtils.createIntBuffer(1);
                    IntBuffer h = BufferUtils.createIntBuffer(1);
                    glfwGetWindowSize(id, w, h);
                    int width = w.get(0), height = h.get(0);

                    ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 3);
                    glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, buffer);

                    int[] pixels = new int[width * height];
                    for (int i = 0; i < pixels.length; i++)
                        pixels[i] = (buffer.get() << 16) + (buffer.get() << 8) + (buffer.get());

                    BufferedImage rawImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    rawImg.setRGB(0, 0, width, height, pixels, 0, width);

                    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    AffineTransform tran = AffineTransform.getTranslateInstance(0, height);
                    AffineTransform flip = AffineTransform.getScaleInstance(1, -1);
                    tran.concatenate(flip);
                    Graphics2D g = img.createGraphics();
                    g.setTransform(tran);
                    g.drawImage(rawImg, 0, 0, null);
                    g.dispose();

                    File file = new File("E:/Windows/Desktop/" + System.currentTimeMillis() + ".png");
                    try {
                        ImageIO.write(img, "png", file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Screenshot saved! " + file.getAbsolutePath());
                }
            }
        });

        //Mouse input
        glfwSetInputMode(window.id(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPosCallback(window.id(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                updateCameraRotation((float) x, (float) y);
            }
        });

        //Clear color
        glClearColor(CLEAR_COLOR, CLEAR_COLOR, CLEAR_COLOR, 1);

        //Shader
        Shader lightingVS = new Shader("./src/main/java/com/nami/shader/shaders/lighting.vert", Shader.ShaderType.VERTEX_SHADER);
        Shader lightingFS = new Shader("./src/main/java/com/nami/shader/shaders/lighting.frag", Shader.ShaderType.FRAGMENT_SHADER);
        lightingShader = new ShaderProgram(lightingVS, lightingFS);
        lightingShader.deleteShaders();

        Shader lightCubeVS = new Shader("./src/main/java/com/nami/shader/shaders/light-cube.vert", Shader.ShaderType.VERTEX_SHADER);
        Shader lightCubeFS = new Shader("./src/main/java/com/nami/shader/shaders/light-cube.frag", Shader.ShaderType.FRAGMENT_SHADER);
        lightCubeShader = new ShaderProgram(lightCubeVS, lightCubeFS);
        lightCubeShader.deleteShaders();

        //Cubes
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, cubeVertices, GL_STATIC_DRAW);

        cubeVAO = glGenVertexArrays();
        glBindVertexArray(cubeVAO);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        cubeModels = new Matrix4f[15];
        float range = 5f;
        for (int i = 0; i < cubeModels.length; i++) {
            Matrix4f model = new Matrix4f();
            model.translate(random(-range, range), random(-range, range), random(-range, range));
            model.scale(0.5f);

            cubeModels[i] = model;
        }

        lightVAO = glGenVertexArrays();
        glBindVertexArray(lightVAO);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        material = new Material(new Texture2D("./src/main/resources/container.png"), new Texture2D("./src/main/resources/container_specular.png"), 64.0f);

        directionalLights = new ArrayList<>();
        DirectionalLight sun = new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.76f));
        //sun.setColor(new LightColor(new Vector3f(0.05f, 0.05f, 0.05f), new Vector3f(0.8f, 0.8f, 0.8f), new Vector3f(0.85f, 0.85f, 0.85f)));
        sun.setColor(LightColor.BLACK);
        directionalLights.add(sun);

        pointLights = new ArrayList<>();
        PointLight point = new PointLight(new Vector3f(0, 0, 0));
        point.setColor(LightColor.RED);
        point.setPoint(LightPoint.D3250);
        pointLights.add(point);

        spotLights = new ArrayList<>();
        torch = new SpotLight(new Vector3f(0f, 0f, 0f));
        torch.setColor(LightColor.WHITE);
        torch.setSpot(new LightSpot(new Vector3f(0f, 0f, 0f), Math.cos(Math.toRadians(3f)), Math.cos(Math.toRadians(5f))));
        torch.setPoint(LightPoint.D200);
        spotLights.add(torch);

        //Camera
        camera = new Camera(settings.user().fov(), settings.window().aspect().aspect(), 0.1f, 1000f);

        double lastFrame = 0;
        int frames = 0;
        int lastTime = 0;

        //Show window
        window.setVisible(true);

        window.setFullscreen(settings.window().fullscreen());

        //Main loop
        while (!window.shouldClose()) {
            glfwPollEvents();

            double currentFrame = glfwGetTime();
            DELTA_TIME = (float) (currentFrame - lastFrame);
            lastFrame = currentFrame;

            processKeys();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Matrix4f projection = camera.getProjectionMatrix();
            Matrix4f view = camera.getViewMatrix();

            torch.getSpot().direction().set(camera.getFront());
            torch.getPosition().set(camera.getPosition());

            //Lighting
            lightingShader.use();

            lightingShader.setUniformVector3f("viewPos", camera.getPosition());
            lightingShader.setUniformMaterial("material", material);

            lightingShader.setUniformDirectionalLights("directionalLights", directionalLights);
            lightingShader.setUniformPointLights("pointLights", pointLights);
            lightingShader.setUniformSpotLights("spotLights", spotLights);

            lightingShader.setUniformMatrix4f("projection", false, projection);
            lightingShader.setUniformMatrix4f("view", false, view);
            for (Matrix4f cubeModel : cubeModels) {
                lightingShader.setUniformMatrix4f("model", false, cubeModel);
                glBindVertexArray(cubeVAO);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }

            //LightCube
            lightCubeShader.use();

            lightCubeShader.setUniformVector3f("color", LightColor.RED.diffuse());
            lightCubeShader.setUniformMatrix4f("projection", false, projection);
            lightCubeShader.setUniformMatrix4f("view", false, view);

            Matrix4f model = new Matrix4f();
            model.translate(new Vector3f(0, 0, 0));
            model.scale(0.1f);
            lightCubeShader.setUniformMatrix4f("model", false, model);
            glBindVertexArray(lightVAO);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            frames++;
            if (lastTime < (int) glfwGetTime()) {
                window.setTitle(frames + " FPS");
                frames = 0;
                lastTime = (int) glfwGetTime();
            }

            window.swapBuffers();
        }

        //Exit
        glfwTerminate();
    }

    private float random(float min, float max) {
        return (float) (min + Math.random() * (max - min));
    }

    private void processKeys() {
        ControlSettings controls = settings.controls();

        //Sprint
        float speed = 10;
        if (glfwGetKey(window.id(), controls.sprint()) == GLFW_PRESS)
            speed *= 3;

        //Zoom

        if (window.getKey(controls.zoom()) == GLFW_PRESS)
            camera.setFovDEG(10);
        if (window.getKey(controls.zoom()) == GLFW_RELEASE)
            camera.setFovDEG(settings.user().fov());

        //Movement
        if (window.getKey(controls.forward()) == GLFW_PRESS)
            camera.move(Camera.CameraMovement.FORWARD, speed, DELTA_TIME);
        if (window.getKey(controls.left()) == GLFW_PRESS)
            camera.move(Camera.CameraMovement.LEFT, speed, DELTA_TIME);
        if (window.getKey(controls.backward()) == GLFW_PRESS)
            camera.move(Camera.CameraMovement.BACKWARD, speed, DELTA_TIME);
        if (window.getKey(controls.right()) == GLFW_PRESS)
            camera.move(Camera.CameraMovement.RIGHT, speed, DELTA_TIME);
        if (window.getKey(controls.up()) == GLFW_PRESS)
            camera.move(Camera.CameraMovement.UP, speed, DELTA_TIME);
        if (window.getKey(controls.down()) == GLFW_PRESS)
            camera.move(Camera.CameraMovement.DOWN, speed, DELTA_TIME);
    }

    private float lastX, lastY;

    public void updateCameraRotation(float x, float y) {
        camera.mouse(x - lastX, lastY - y, true, settings.user().sensivity());

        lastX = x;
        lastY = y;
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
