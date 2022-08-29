package com.igrium.videolib.util;

import java.net.URL;

/**
 * General utility functions related to VideoLib.
 */
public class VideoLibUtils {
    
    /**
     * <code>URL</code>'s default <code>toString()</code> function returns a URL
     * that is incompatible with VLC, likely due to standard differences. This
     * modified function attempts to fix this and produce a valid VLC MRL.
     * 
     * @param url The URL.
     * @return The URL as a string.
     */
    public static String fixUrl(URL url) {
        String s;
        return url.getProtocol()
                + "://"
                + ((s = url.getAuthority()) != null && !s.isEmpty() ? s : "")
                + ((s = url.getPath()) != null ? s : "")
                + ((s = url.getQuery()) != null ? '?' + s : "")
                + ((s = url.getRef()) != null ? '#' + s : "");
    }
}
