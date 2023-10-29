package com.nami.settings;

public record WindowSettings(int width, int height, AspectRatio aspect, int monitor, int fps, boolean vsync, boolean fullscreen) {

}
