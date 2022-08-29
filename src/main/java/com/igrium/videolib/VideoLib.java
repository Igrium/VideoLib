package com.igrium.videolib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.igrium.videolib.api.VideoHandleFactory;
import com.igrium.videolib.api.VideoManager;
import com.igrium.videolib.api.VideoPlayer;
import com.igrium.videolib.api.VideoManager.VideoManagerFactory;
import com.igrium.videolib.config.VideoLibConfig;
import com.igrium.videolib.util.AfterInitCallback;
import com.igrium.videolib.util.MissingNativesException;
import com.igrium.videolib.vlc.VLCVideoManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * The main class for VideoLib.
 */
public final class VideoLib implements ClientModInitializer {
    public static final Registry<VideoManagerFactory> VIDEO_MANAGERS = FabricRegistryBuilder
            .createSimple(VideoManagerFactory.class, new Identifier("videolib", "managers")).buildAndRegister();
    private static Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("videolib.json");

    private static VideoLib instance;
    private static Logger LOGGER = LogManager.getLogger();
    
    /**
     * Get the current VideoLib instance.
     * @return VideoLib instance
     */
    public static VideoLib getInstance() {
        return instance;
    }

    private VideoLibConfig config = new VideoLibConfig();

    /**
     * Get the VideoLib configuration data. Rarely useful except internally.
     * @return VideoLib config.
     */
    public VideoLibConfig getConfig() {
        return config;
    }

    private VideoManager videoManager;
    private VideoPlayer defaultPlayer;

    /**
     * Get the active video manager.
     * @return The video manager.
     */
    public VideoManager getVideoManager() {
        return videoManager;
    }

    /**
     * Get the active video handle factory. Shortcut for
     * <code>getVideoManager().getHandleFactory()</code>
     * 
     * @return The video handle factory.
     */
    public VideoHandleFactory getHandleFactory() {
        return videoManager.getVideoHandleFactory();
    }

    /**
     * Get a "global" video player that can be used by default. Whenever there isn't
     * the need for multiple videos playing at once, this reduces the overhead of
     * spawning and removing video players whenever they're needed. To ensure mod
     * compatibility, this should generally only be used in the UI.
     * 
     * @return Default video player.
     */
    public VideoPlayer getDefaultPlayer() {
        return defaultPlayer;
    }

    /**
     * Internal use only.
     */
    public void onInitializeClient() {
        instance = this;
        readConfig();

        AfterInitCallback.EVENT.register(client -> initVideoManager());

        // Default video manager.
        Registry.register(VIDEO_MANAGERS, new Identifier("videolib", "vlcj"), VLCVideoManager::new);
    }
    
    private void readConfig() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE.toFile()));
            config = VideoLibConfig.fromJson(reader);
            reader.close();
        } catch (IOException e) {
            LOGGER.error("Unable to load VideoLib config!", e);
        }
        
    }

    private void initVideoManager() {
        try {
            Identifier implementation = getConfig().getImplementation();
            VideoManagerFactory factory = VIDEO_MANAGERS.get(implementation);
            if (factory == null) {
                throw new MissingNativesException("Unknown video implementation: "+implementation);
            }
            initVideoManager(factory.create());

            LOGGER.info("Initialized video implementation: {}", implementation);
        } catch (MissingNativesException e) {
            LOGGER.error("Unable to init VideoLib.", e);
        }
        
    }

    private void initVideoManager(VideoManager videoManager) {
        this.videoManager = videoManager;
        this.defaultPlayer = videoManager.getOrCreate(new Identifier("videolib", "default"));

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(videoManager.getReloadListener());
    }
}
