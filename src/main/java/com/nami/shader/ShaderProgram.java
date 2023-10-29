package com.nami.shader;

import com.nami.light.DirectionalLight;
import com.nami.light.PointLight;
import com.nami.Material;
import com.nami.light.SpotLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {

    private final int id;
    private final Shader[] shaders;

    public ShaderProgram(Shader... shaders) throws IOException {
        this.id = glCreateProgram();
        this.shaders = shaders;

        for (Shader shader : shaders)
            glAttachShader(id, shader.ID());

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) != GL_TRUE)
            throw new IOException(glGetProgramInfoLog(id));
    }

    public void setUniformMatrix4f(String name, boolean transpose, Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(getUniformLocation(name), transpose, buffer);
    }

    public void setUniformVector3f(String name, Vector3f vec) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
        vec.get(buffer);
        glUniform3fv(getUniformLocation(name), buffer);
    }

    public void setUniformFloat(String name, float f) {
        glUniform1f(getUniformLocation(name), f);
    }

    public void setUniformInt(String name, int i) {
        glUniform1i(getUniformLocation(name), i);
    }

    public void setUniformDirectionalLights(String name, List<DirectionalLight> lights) {
        for(int i = 0; i < lights.size(); i++) {
            DirectionalLight light = lights.get(i);
            String aName = name.concat(String.format("[%s]", i));

            setUniformVector3f(aName.concat(".direction"), light.getDirection());

            setUniformVector3f(aName.concat(".ambient"), light.getColor().ambient());
            setUniformVector3f(aName.concat(".diffuse"), light.getColor().diffuse());
            setUniformVector3f(aName.concat(".specular"), light.getColor().specular());
        }
    }

    public void setUniformPointLights(String name, List<PointLight> lights) {
        for(int i = 0; i < lights.size(); i++) {
            PointLight light = lights.get(i);
            String aName = name.concat(String.format("[%s]", i));

            setUniformVector3f(aName.concat(".position"), light.getPosition());

            setUniformVector3f(aName.concat(".ambient"), light.getColor().ambient());
            setUniformVector3f(aName.concat(".diffuse"), light.getColor().diffuse());
            setUniformVector3f(aName.concat(".specular"), light.getColor().specular());

            setUniformFloat(aName.concat(".constant"), light.getPoint().constant());
            setUniformFloat(aName.concat(".linear"), light.getPoint().linear());
            setUniformFloat(aName.concat(".quadratic"), light.getPoint().quadratic());
        }
    }

    public void setUniformSpotLights(String name, List<SpotLight> lights) {
        for(int i = 0; i < lights.size(); i++) {
            SpotLight light = lights.get(i);
            String aName = name.concat(String.format("[%s]", i));

            setUniformVector3f(aName.concat(".position"), light.getPosition());

            setUniformVector3f(aName.concat(".ambient"), light.getColor().ambient());
            setUniformVector3f(aName.concat(".diffuse"), light.getColor().diffuse());
            setUniformVector3f(aName.concat(".specular"), light.getColor().specular());

            setUniformFloat(aName.concat(".constant"), light.getPoint().constant());
            setUniformFloat(aName.concat(".linear"), light.getPoint().linear());
            setUniformFloat(aName.concat(".quadratic"), light.getPoint().quadratic());

            setUniformVector3f(aName.concat(".direction"), light.getSpot().direction());
            setUniformFloat(aName.concat(".cutOff"), light.getSpot().cutOff());
            setUniformFloat(aName.concat(".outerCutOff"), light.getSpot().outerCutOff());
        }
    }

    public void setUniformMaterial(String name, Material material) {
        setUniformInt(name.concat(".diffuse"), 0);
        glActiveTexture(GL_TEXTURE0);
        material.diffuseMap().bind();

        setUniformInt(name.concat(".specular"), 1);
        glActiveTexture(GL_TEXTURE1);
        material.specularMap().bind();

        setUniformFloat(name.concat(".shininess"), material.shininess());
    }

    private int getUniformLocation(String name) {
        return glGetUniformLocation(id, name);
    }

    public void deleteShaders() {
        for (Shader s : shaders)
            s.delete();
    }

    public void use() {
        glUseProgram(id);
    }

}
