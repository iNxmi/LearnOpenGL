package com.nami.light;

public abstract class Light {

    LightColor color = LightColor.WHITE;

    public void setColor(LightColor color) {
        this.color = color;
    }

    public LightColor getColor() {
        return color;
    }

}
