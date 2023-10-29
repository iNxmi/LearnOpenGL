package com.nami.light;

import org.joml.Vector3f;

public class SpotLight extends Light {

    private Vector3f position;
    private LightPoint point;
    private LightSpot spot;

    public SpotLight(Vector3f position) {
        this.position = position;
        this.point = LightPoint.D50;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPoint(LightPoint point) {
        this.point = point;
    }

    public LightPoint getPoint() {
        return point;
    }

    public void setSpot(LightSpot spot) {
        this.spot = spot;
    }

    public LightSpot getSpot() {
        return spot;
    }

}
