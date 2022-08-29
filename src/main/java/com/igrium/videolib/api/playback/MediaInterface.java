package com.igrium.videolib.api.playback;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Optional;

import com.igrium.videolib.api.VideoHandle;
import com.igrium.videolib.api.VideoHandleFactory;

import net.minecraft.util.Identifier;

/**
 * Behavior pertaining to the loading and playback of video media.
 */
public interface MediaInterface {

    /**
     * Load a video and prepare it for playback.
     * @param handle Video handle.
     * @return Success.
     */
    public boolean load(VideoHandle handle);

    public default boolean load(String url) throws MalformedURLException, URISyntaxException {
        return load(getVideoHandleFactory().getVideoHandle(url));
    }

    public default boolean load(Identifier id) {
        return load(getVideoHandleFactory().getVideoHandle(id));
    }

    /**
     * Load a video and play it.
     * 
     * @param handle Video handle.
     * @return Success.
     * @throws IllegalArgumentException If this this video player is incompatible
     *                                  with this handle. Use
     *                                  <code>getHandle()</code> to get a handle
     *                                  guarenteed to work with this player.
     */
    public boolean play(VideoHandle handle) throws IllegalArgumentException;

    public default boolean play(String url) throws MalformedURLException, URISyntaxException {
        return play(getVideoHandleFactory().getVideoHandle(url));
    }

    public default boolean play(Identifier id) {
        return play(getVideoHandleFactory().getVideoHandle(id));
    }

    /**
     * Check whether there is currently a video loaded.
     * @return Is there a video loaded?
     */
    public boolean hasMedia();

    /**
     * Get the handle of the currently loaded video. Note: most implementations will
     * attempt to reverse-engineer the handle from the native player. If you need
     * the original handle, keep track of it yourself.
     * 
     * @return An optional with the handle.
     */
    public Optional<VideoHandle> currentMedia();

    /**
     * Get the factory that should be used to generate video handles for this
     * player.
     * 
     * @return Video handle factory.
     */
    public VideoHandleFactory getVideoHandleFactory();
}
