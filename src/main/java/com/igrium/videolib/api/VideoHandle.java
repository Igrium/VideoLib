package com.igrium.videolib.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import com.igrium.videolib.util.VideoLibUtils;

/**
 * A reference to a video that can be played by a media player.
 */
public interface VideoHandle {

    /**
     * Get an accessible URL that can be passed directly to a playback
     * implementation.
     * 
     * @return The address, or an empty optional if this video can only be accessed
     *         through the handle's <code>openStream</code> function.
     */
    Optional<String> getAddress();

    /**
     * Open an input stream with the contents of this video file.
     * @return The input stream.
     * @throws IOException If the stream cannot be opened.
     */
    InputStream openStream() throws IOException;

    /**
     * A video handle based on a given URL.
     */
    public static class UrlVideoHandle implements VideoHandle {

        private final URL url;

        public UrlVideoHandle(URL url) {
            this.url = url;
        }

        @Override
        public Optional<String> getAddress() {
            return Optional.of(VideoLibUtils.fixUrl(url));
        }

        @Override
        public InputStream openStream() throws IOException {
            return url.openStream();
        }

    }

    /**
     * A video handle based on a given file.
     */
    public static class FileVideoHandle implements VideoHandle {
        private final File file;

        public FileVideoHandle(File file) {
            this.file = file;
        }

        @Override
        public Optional<String> getAddress() {
            try {
                return Optional.of(VideoLibUtils.fixUrl(file.toURI().toURL()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e); // Should never happen.
            }
        }

        @Override
        public InputStream openStream() throws FileNotFoundException {
            return new FileInputStream(file);
        }
    }
}
