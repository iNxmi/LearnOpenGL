package com.nami.light;

import org.joml.Vector3f;

public record LightSpot(Vector3f direction, float cutOff, float outerCutOff) {

}
