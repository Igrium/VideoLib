package com.igrium.videolib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.igrium.videolib.api.VideoHandle;
import com.igrium.videolib.api.VideoHandleFactory;
import com.igrium.videolib.api.VideoManager;
import com.igrium.videolib.api.VideoPlayer;
import com.igrium.videolib.api.VideoManager.VideoManagerFactory;
import com.igrium.videolib.config.VideoLibConfig;
import com.igrium.videolib.dummy.DummyVideoManager;
import com.igrium.videolib.render.VideoScreen;
import com.igrium.videolib.server.VideoLibNetworking;
import com.igrium.videolib.server.VideoLibNetworking.InstallStatus;
import com.igrium.videolib.server.VideoLibNetworking.PlaybackCommand;
import com.igrium.videolib.util.AfterInitCallback;
import com.igrium.videolib.util.MissingNativesException;
import com.igrium.videolib.vlc.VLCVideoManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * The main class for VideoLib.
 */
@Environment(EnvType.CLIENT)
public final class VideoLib implements ClientModInitializer {
    public static final Registry<VideoManagerFactory> VIDEO_MANAGERS = FabricRegistryBuilder
            .createSimple(VideoManagerFactory.class, new Identifier("videolib", "managers")).buildAndRegister();
    private static Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("videolib.json");

    private static VideoLib instance;
    private static Logger LOGGER = LogManager.getLogger();
    private MinecraftClient client = MinecraftClient.getInstance();
    
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

        // Networking
        ClientPlayConnectionEvents.JOIN.register((networkHandler, packetSender, client) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeEnumConstant(getInstallStatus());
            packetSender.sendPacket(VideoLibNetworking.SYNC_VIDEOLIB_STATUS, buf);
        });
        
        ClientPlayNetworking.registerGlobalReceiver(VideoLibNetworking.PLAYBACK_COMMAND,
            (client, handler, buf, responseSender) -> {
                PlaybackCommand command = PlaybackCommand.fromByteBuf(buf);
                client.execute(() -> parsePlaybackCommand(command));
            });

        // Default video manager.
        Registry.register(VIDEO_MANAGERS, new Identifier("videolib", "vlcj"), VLCVideoManager::new);
        
    }

    /**
     * Parse a playback command with a given video player.
     * @param player Video player to use.
     * @param command Playback command to parse.
     */
    public static void parsePlaybackCommand(VideoPlayer player, PlaybackCommand command) {
        if (command.shouldStop()) {
            player.getControlsInterface().stop();
            return;
        }

        if (command.id().isPresent() || command.url().isPresent()) {
            VideoHandle handle;
            if (command.id().isPresent()) {
                handle = player.getHandleFactory().getVideoHandle(command.id().get());
            } else {
                handle = player.getHandleFactory().getVideoHandle(command.url().get());
            }

            player.getMediaInterface().play(handle);
        }

        player.getControlsInterface().setPause(command.isPaused());

        if (command.timeMillis().isPresent()) {
            player.getControlsInterface().setTime(command.timeMillis().get());
        }
    }

    /**
     * Parse a playback command with the fullscreen video player.
     * @param command Video player to use.
     * @see #parsePlaybackCommand(VideoPlayer, PlaybackCommand)
     */
    public void parsePlaybackCommand(PlaybackCommand command) {
        VideoScreen screen;

        if (client.currentScreen instanceof VideoScreen) {
            screen = (VideoScreen) client.currentScreen;
        } else if (command.shouldOpenScreen()) {
            screen = new VideoScreen(getDefaultPlayer());
            client.setScreen(screen);
        } else {
            LOGGER.warn("Recieved video playback command while no video is playing: {}", command);
            return;
        }

        parsePlaybackCommand(screen.getPlayer(), command);
    }
    
    /**
     * Get the VideoLib install status of the local client.
     * @return VideoLib install status.
     */
    public InstallStatus getInstallStatus() {
        return getVideoManager().hasNatives() ? InstallStatus.INSTALLED : InstallStatus.MISSING_NATIVES;
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
            initVideoManager(new DummyVideoManager());
        }
        
    }

    private void initVideoManager(VideoManager videoManager) {
        this.videoManager = videoManager;
        this.defaultPlayer = videoManager.getOrCreate(new Identifier("videolib", "default"));

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(videoManager.getReloadListener());
    }
}
