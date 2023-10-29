package com.nami.light;

import org.joml.Vector3f;

public class DirectionalLight extends Light {

    private Vector3f direction;

    public DirectionalLight(Vector3f direction) {
        this.direction = direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getDirection() {
        return direction;
    }

}
