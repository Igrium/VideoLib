package com.igrium.videolib.api.playback;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.igrium.videolib.api.VideoHandle;

import net.minecraft.util.Identifier;

/**
 * Behavior pertaining to the loading and playback of video media.
 * @param <T> The type that's expected for the video handle.
 */
public interface MediaInterface<T extends VideoHandle> {

    /**
     * Load a video and prepare it for playback.
     * @param handle Video handle.
     * @return Success.
     */
    public boolean load(T handle);

    public default boolean load(String uri) throws MalformedURLException, URISyntaxException {
        return load(getHandle(uri));
    }

    public default boolean load(Identifier id) {
        return load(getHandle(id));
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

    public default boolean play(String uri) throws MalformedURLException, URISyntaxException {
        return play(getHandle(uri));
    }

    public default boolean play(Identifier id) {
        return play(getHandle(id));
    }

    /**
     * Check whether there is currently a video loaded.
     * @return Is there a video loaded?
     */
    public boolean hasMedia();

    /**
     * Get the handle of the currently loaded video.
     * @return An optional with the handle.
     */
    public Optional<T> currentMedia();

    /**
     * Get a media handle that this player will support from an identifier.
     * 
     * @param id ID to use.
     * @return The handle.
     */
    public T getHandle(Identifier id);

    /**
     * Get a media handle that this player will support from a uri.
     * 
     * @param uri Uri to use.
     * @return The handle.
     * @throws MalformedURLException If the URL protocol is not supported.
     */
    public T getHandle(URI uri) throws MalformedURLException;

    public default T getHandle(String uri) throws MalformedURLException, URISyntaxException {
        return getHandle(new URI(uri));
    }
}
