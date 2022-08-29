package com.igrium.videolib.api;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nullable;

import net.minecraft.util.Identifier;

/**
 * Creates or loads video handles that are compatible with a given video manager.
 */
public interface VideoHandleFactory {
    
    /**
     * Get a video handle from an identifier.
     * @param id The identifier.
     * @return The handle, or <code>null</code> if no video by this ID exists.
     */
    @Nullable
    VideoHandle getVideoHandle(Identifier id);
    
    /**
     * Get a video handle from a URL.
     * @param url The URL.
     * @return The handle.
     */
    VideoHandle getVideoHandle(URL url);

    /**
     * Get a video from a URL in string form.
     * @param url The URL.
     * @return The handle.
     * @throws MalformedURLException If URL is improperly formatted.
     */
    default VideoHandle getVideoHandle(String url) throws MalformedURLException {
        return getVideoHandle(new URL(url));
    }
}
