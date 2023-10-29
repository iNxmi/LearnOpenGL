package com.nami.light;

import org.joml.Vector3f;

public record LightColor(Vector3f ambient, Vector3f diffuse, Vector3f specular) {

    private static final float ambientMod = 0.1f;
    private static final float diffuseMod = 0.8f;
    private static final float specularMod = 1.0f;
    public static final LightColor
            BLACK = new LightColor(new Vector3f(0f, 0f, 0f), ambientMod, diffuseMod, specularMod),
            BLUE = new LightColor(new Vector3f(0f, 0f, 1f), ambientMod, diffuseMod, specularMod),
            GREEN = new LightColor(new Vector3f(0f, 1f, 0f), ambientMod, diffuseMod, specularMod),
            AQUA = new LightColor(new Vector3f(0f, 1f, 1f), ambientMod, diffuseMod, specularMod),
            RED = new LightColor(new Vector3f(1f, 0f, 0f), ambientMod, diffuseMod, specularMod),
            PINK = new LightColor(new Vector3f(1f, 0f, 1f), ambientMod, diffuseMod, specularMod),
            YELLOW = new LightColor(new Vector3f(1f, 1f, 0f), ambientMod, diffuseMod, specularMod),
            WHITE = new LightColor(new Vector3f(1f, 1f, 1f), ambientMod, diffuseMod, specularMod);


    LightColor(Vector3f color, float ambientMod, float diffuseMod, float specularMod) {
        this(new Vector3f(color).mul(ambientMod), new Vector3f(color).mul(diffuseMod), new Vector3f(color).mul(specularMod));
    }

}
