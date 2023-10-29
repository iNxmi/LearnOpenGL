package com.nami.settings;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public record Settings(WindowSettings window, UserSettings user, RenderSettings render, ControlSettings controls) {

    public static Settings load(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(path).toFile(), Settings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
