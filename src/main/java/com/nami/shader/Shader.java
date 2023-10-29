package com.nami.shader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

    private int id;

    public Shader(String path, ShaderType shaderType) throws IOException {
        this.id = glCreateShader(shaderType.getId());

        File file = new File(path);
        byte[] data = Files.readAllBytes(file.toPath());
        String shaderCode = new String(data);
        glShaderSource(id, shaderCode);

        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) != GL_TRUE)
            throw new IOException(glGetShaderInfoLog(id));
    }

    public void delete() {
        glDeleteShader(id);
    }

    public int ID() {
        return id;
    }

    public enum ShaderType {
        VERTEX_SHADER(GL_VERTEX_SHADER), FRAGMENT_SHADER(GL_FRAGMENT_SHADER);

        private int id;

        ShaderType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

}
