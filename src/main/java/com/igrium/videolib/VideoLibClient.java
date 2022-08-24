package com.igrium.videolib;

import com.igrium.videolib.api.MediaManager;
import com.igrium.videolib.test.AddVideoPlayerCommand;
import com.igrium.videolib.test.TestRenderDispatcher;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class VideoLibClient implements ClientModInitializer {

    private static VideoLibClient instance;

    private MediaManager mediaManager;
    private TestRenderDispatcher testRenderDispatcher;

    public static VideoLibClient getInstance() {
        return instance;
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    public TestRenderDispatcher getTestRenderDispatcher() {
        return testRenderDispatcher;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        mediaManager = new MediaManager();
        testRenderDispatcher = new TestRenderDispatcher();

        WorldRenderEvents.AFTER_ENTITIES.register(testRenderDispatcher::render);
        AddVideoPlayerCommand.register(ClientCommandManager.DISPATCHER);
    }
    
}
