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
}
