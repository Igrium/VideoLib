package com.igrium.videolib.api.playback;

/**
 * Technical information about the current video.
 */
public interface CodecInterface {

    /**
     * Get the width of the video.
     * @return Width in pixels.
     */
    public int getWidth();

    /**
     * Get the height of the video.
     * @return Height in pixels.
     */
    public int getHeight();

    /**
     * Get the aspect ratio of this video.
     * 
     * @return The aspect ratio in the form <code>[x]:1</code>, where <code>x</code>
     *         is this return value. If the video uses square pixels, this is
     *         equivilent to <code>getWidth() / getHeight()</code>
     */
    public default float getAspectRatio() {
        if (getHeight() == 0) {
            return 1;
        }
        return ((float) getWidth()) / getHeight();
    }

    /**
     * Get the frame rate of this video.
     * @return Frames per second.
     */
    public float getFrameRate();
}
