package com.nami.light;

public record LightPoint(float constant, float linear, float quadratic) {

    public static final LightPoint
            D7 = new LightPoint(1.0f, 0.7f, 1.8f),
            D13 = new LightPoint(1.0f, 0.35f, 0.44f),
            D20 = new LightPoint(1.0f, 0.22f, 0.20f),
            D32 = new LightPoint(1.0f, 0.14f, 0.07f),
            D50 = new LightPoint(1.0f, 0.09f, 0.032f),
            D65 = new LightPoint(1.0f, 0.07f, 0.017f),
            D100 = new LightPoint(1.0f, 0.045f, 0.0075f),
            D160 = new LightPoint(1.0f, 0.027f, 0.0028f),
            D200 = new LightPoint(1.0f, 0.022f, 0.0019f),
            D325 = new LightPoint(1.0f, 0.014f, 0.0007f),
            D600 = new LightPoint(1.0f, 0.007f, 0.0002f),
            D3250 = new LightPoint(1.0f, 0.0014f, 0.000007f);

}
