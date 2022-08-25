package com.igrium.videolib;

import com.igrium.videolib.api.VideoManager;
import com.igrium.videolib.api.VideoPlayer;
import com.igrium.videolib.test.AddVideoPlayerCommand;
import com.igrium.videolib.test.TestRenderDispatcher;
import com.igrium.videolib.util.AfterInitCallback;
import com.igrium.videolib.vlc.VLCVideoManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class VideoLibClient implements ClientModInitializer {

    private static VideoLibClient instance;

    private TestRenderDispatcher testRenderDispatcher;
    private VideoManager videoManager;
    private VideoPlayer mainPlayer;

    public static VideoLibClient getInstance() {
        return instance;
    }

    // public VideoManager getVideoManager() {
    //     return videoManager;
    // }

    // public VideoPlayer getMainPlayer() {
    //     return mainPlayer;
    // }

    public TestRenderDispatcher getTestRenderDispatcher() {
        return testRenderDispatcher;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        testRenderDispatcher = new TestRenderDispatcher();

        // initVideoManager(new VLCVideoManager());

        WorldRenderEvents.AFTER_ENTITIES.register(testRenderDispatcher::render);
        AddVideoPlayerCommand.register(ClientCommandManager.DISPATCHER);

        AfterInitCallback.EVENT.register(client -> {
        });
    }

    protected void initVideoManager(VideoManager videoManager) {
        this.videoManager = videoManager;
        mainPlayer = videoManager.getOrCreate(new Identifier("videolib", "default"));

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(videoManager.getReloadListener());
    }
    
}
