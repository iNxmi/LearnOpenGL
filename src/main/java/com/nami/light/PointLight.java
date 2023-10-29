package com.nami.light;

import org.joml.Vector3f;

public class PointLight extends Light {

    private Vector3f position;
    private LightPoint point;

    public PointLight(Vector3f position) {
        this.position = position;
        this.point = LightPoint.D50;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPoint(LightPoint spread) {
        this.point = point;
    }

    public LightPoint getPoint() {
        return point;
    }

}
