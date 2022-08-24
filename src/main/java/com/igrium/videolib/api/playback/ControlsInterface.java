package com.igrium.videolib.api.playback;

public interface ControlsInterface {

    /**
     * Begin (or resume) playback.
     */
    public void play();
    
    /**
     * Stop playback. Subsequent play will start from the beginning.
     */
    public void stop();

    /**
     * Pause or resume.
     * @param pause True to pause; false to resume.
     */
    public void setPause(boolean pause);

    /**
     * Pause playback.
     */
    public default void pause() {
        setPause(true);
    }

    /**
     * Resume playback.
     */
    public default void resume() {
        setPause(false);
    }

    /**
     * Jump to a specific time.
     * @param time Time since the beginning in milliseconds.
     */
    public void setTime(long time);

    /**
     * Get the current time of the playhead.
     * @return Time since the beginning in milliseconds.
     */
    public long getTime();

    /**
     * Get the length of the current video.
     * @return Video length in milliseconds.
     */
    public long getLength();

    /**
     * Set whether this video should loop back to the beginning when its finished.
     * @param repeat Should repeat
     */
    public void setRepeat(boolean repeat);

    /**
     * Get whether this video will loop back to the beginning when its finished.
     * @return Should repeat.
     */
    public boolean repeat();

    /**
     * Attempt to set the rate of playback. Not all implementations or codecs support this.
     * @param rate Playback rate.
     * @return Was the rate successfully changed?
     */
    public default boolean setRate(float rate) {
        return false;
    }

    /**
     * Get the current rate of playback.
     * @return Current playback rate.
     */
    public default float getRate() {
        return 1;
    }
    
}
