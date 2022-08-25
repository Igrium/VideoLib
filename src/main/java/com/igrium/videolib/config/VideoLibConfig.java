package com.igrium.videolib.config;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.igrium.videolib.util.IdentifierGson;

import net.minecraft.util.Identifier;

public class VideoLibConfig {
    private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(Identifier.class, new IdentifierGson()).create();

    private Identifier implementation = new Identifier("videolib", "vlcj");

    public Identifier getImplementation() {
        return implementation;
    }

    public void setImplementation(Identifier videoManager) {
        this.implementation = videoManager;
    }

    public static VideoLibConfig fromJson(Reader reader) {
        return GSON.fromJson(reader, VideoLibConfig.class);
    }

    public static VideoLibConfig fromJson(String string) {
        return GSON.fromJson(string, VideoLibConfig.class);
    }

    public static String toJson(VideoLibConfig config) {
        return GSON.toJson(config);
    }
}
