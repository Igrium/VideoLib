package com.igrium.videolib.api;

import java.io.IOException;
import java.io.InputStream;

/**
 * A reference to a video that can be loaded by a video player.
 */
public interface VideoHandle {

    /**
     * Check whether the resource this handle is referencing exists.
     * @return Does this resource exist?
     */
    public boolean exists();

    /**
     * Open an input stream of the raw data that this handle references. The format of this data depends on the implementation.
     * @return The newly-opened stream.
     * @throws IOException If an IO exception occurs while opening the stream.
     */
    public InputStream openStream() throws IOException;
}
