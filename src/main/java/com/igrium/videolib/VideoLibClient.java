package com.igrium.videolib;

import net.fabricmc.api.ClientModInitializer;

public class VideoLibClient implements ClientModInitializer {

    private static VideoLibClient instance;

    public static VideoLibClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
    }
    
}
