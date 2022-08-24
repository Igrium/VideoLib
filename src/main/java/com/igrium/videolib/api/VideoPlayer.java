package com.igrium.videolib.api;

import com.igrium.videolib.api.playback.ControlsInterface;
import com.igrium.videolib.api.playback.MediaInterface;

import net.minecraft.util.Identifier;

/**
 * Loads and plays a video.
 */
public interface VideoPlayer extends AutoCloseable {

    /**
     * Get the identifier of this video player.
     * @return Video player ID.
     */
    public Identifier getId();

    /**
     * Get the texture that this video player will write to.
     * @return Texture ID.
     */
    public Identifier getTexture();

    public MediaInterface<?> getMediaInterface();
    public ControlsInterface getControlsInterface();

    /**
     * Generate a texture identifier from a video player identifier. This is just an
     * internal utility function. It's not guarenteed that any given video player
     * will use this scheme. Use {@link VideoPlayer#getTexture()} instead.
     * 
     * @param id Video player ID.
     * @return Texture ID.
     */
    public static Identifier getTextureId(Identifier id) {
        String namespace = id.getNamespace();
        String path = "videoplayers/"+id.getPath();
        return new Identifier(namespace, path);
    }
}
