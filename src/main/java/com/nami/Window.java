package com.nami;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;

    private int fps, msaa;

    private long id = 0, monitor;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void init(int fps, int msaa, int monitor) {
        this.fps = fps;
        this.msaa = msaa;

        //Initializing GLFW
        if (!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW");
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_REFRESH_RATE, fps);
        if (msaa > 1)
            glfwWindowHint(GLFW_SAMPLES, msaa);

        this.monitor = glfwGetMonitors().get(monitor);

        //Creating window
        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (id == 0)
            throw new IllegalStateException("Failed to create Window");
        glfwMakeContextCurrent(id);

        //Centering window
        GLFWVidMode vidMode = getVidMode();
        if (vidMode == null)
            throw new IllegalStateException("Failed to obtain GLFWVidMode");
        glfwSetWindowPos(id, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        //Creating OpenGL capabilities
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        if (msaa > 1)
            glEnable(GL_MULTISAMPLE);

        //Callback for resizing viewport
        glViewport(0, 0, width, height);
        glfwSetFramebufferSizeCallback(id, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        });
    }

    public void setVisible(boolean visible) {
        if (visible)
            glfwShowWindow(id);
        else
            glfwHideWindow(id);
    }

    public void setFullscreen(boolean fullscreen) {
        GLFWVidMode vidMode = getVidMode();

        if (fullscreen) {
            glfwSetWindowMonitor(id, monitor, 0, 0, vidMode.width(), vidMode.height(), fps);
        } else {
            glfwSetWindowMonitor(id, NULL, vidMode.width() / 2 - width / 2, vidMode.height() / 2 - height / 2, width, height, fps);
        }
    }

    public void shouldClose(boolean bool) {
        glfwSetWindowShouldClose(id, bool);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(id);
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(id, title);
    }

    public void swapBuffers() {
        glfwSwapBuffers(id);
    }

    public int getKey(int key) {
        return glfwGetKey(id, key);
    }

    public GLFWVidMode getVidMode() {
        return glfwGetVideoMode(monitor);
    }

    public long id() {
        return id;
    }

}
