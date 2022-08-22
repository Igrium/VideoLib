package com.igrium.videolib;

import com.igrium.videolib.api.MediaManager;

import net.fabricmc.api.ClientModInitializer;

public class VideoLibClient implements ClientModInitializer {

    private static VideoLibClient instance;

    private MediaManager mediaManager;

    public static VideoLibClient getInstance() {
        return instance;
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        mediaManager = new MediaManager();
    }
    
}
