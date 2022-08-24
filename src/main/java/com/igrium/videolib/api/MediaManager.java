package com.igrium.videolib.api;

import com.igrium.videolib.render.OpenGLVideoSurface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaManager {
    public static final Identifier TEXTURE_ID = new Identifier("videolib", "media");

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private OpenGLVideoSurface surface;

    private boolean isSetup = false;

    public MediaManager() {
        factory = new MediaPlayerFactory();
    }

    public void setup() {
        if (isSetup) return;

        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        surface = new OpenGLVideoSurface();
        mediaPlayer.videoSurface().set(surface);

        MinecraftClient.getInstance().getTextureManager().registerTexture(TEXTURE_ID, surface.getTexture());
    }

    public AbstractTexture getTexture() {
        return surface.getTexture();
    }

    public MediaPlayerFactory getFactory() {
        return factory;
    }

    public EmbeddedMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    
}
