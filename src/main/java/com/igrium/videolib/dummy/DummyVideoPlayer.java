package com.igrium.videolib.dummy;

import java.util.Optional;
import java.util.function.Consumer;

import com.igrium.videolib.api.VideoHandle;
import com.igrium.videolib.api.VideoHandleFactory;
import com.igrium.videolib.api.VideoPlayer;
import com.igrium.videolib.api.playback.CodecInterface;
import com.igrium.videolib.api.playback.ControlsInterface;
import com.igrium.videolib.api.playback.MediaInterface;
import com.igrium.videolib.api.playback.VideoEvents;

import net.minecraft.util.Identifier;

public class DummyVideoPlayer implements VideoPlayer {

    public static final Identifier NO_NATIVES_TEXTURE = new Identifier("videolib", "textures/no_natives.png");

    private final DummyVideoManager manager;

    private final DummyMediaInterface mediaInterface = new DummyMediaInterface();
    private final DummyControlsInterface controlsInterface = new DummyControlsInterface();
    private final DummyCodecInterface codecInterface = new DummyCodecInterface();
    private final DummyVideoEvents events = new DummyVideoEvents();

    public DummyVideoPlayer(DummyVideoManager manager) {
        this.manager = manager;
    }

    @Override
    public void close() {        
    }

    @Override
    public Identifier getId() {
        return new Identifier("videolib", "dummy");
    }

    @Override
    public Identifier getTexture() {
        return NO_NATIVES_TEXTURE;
    }

    @Override
    public MediaInterface getMediaInterface() {
        return mediaInterface;
    }

    @Override
    public ControlsInterface getControlsInterface() {
        return controlsInterface;
    }

    @Override
    public CodecInterface getCodecInterface() {
        return codecInterface;
    }

    @Override
    public VideoEvents getEvents() {
        return events;
    }

    private class DummyMediaInterface implements MediaInterface {

        @Override
        public boolean load(VideoHandle handle) {
            return false;
        }

        @Override
        public boolean play(VideoHandle handle) throws IllegalArgumentException {
            return false;
        }

        @Override
        public boolean hasMedia() {
            return false;
        }

        @Override
        public Optional<VideoHandle> currentMedia() {
            return Optional.empty();
        }

        @Override
        public VideoHandleFactory getVideoHandleFactory() {
            return manager.getVideoHandleFactory();
        }
        
    }

    private class DummyControlsInterface implements ControlsInterface {

        @Override
        public void play() {}

        @Override
        public void stop() {}

        @Override
        public void setPause(boolean pause) {}

        @Override
        public void setTime(long time) {}

        @Override
        public long getTime() {
            return 0;
        }

        @Override
        public long getLength() {
            return 0;
        }

        @Override
        public void setRepeat(boolean repeat) {}

        @Override
        public boolean repeat() {
            return false;
        }
        
    }

    private class DummyCodecInterface implements CodecInterface {

        // The resolution of the no-natives texture
        @Override
        public int getWidth() {
            return 128;
        }

        @Override
        public int getHeight() {
            return 128;
        }

        @Override
        public float getFrameRate() {
            return 0;
        }
        
    }

    private class DummyVideoEvents implements VideoEvents {

        @Override
        public void onOpening(Consumer<Void> listener) {}

        @Override
        public boolean removeOnOpening(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onBuffering(Consumer<BufferingEvent> listener) {}

        @Override
        public boolean removeOnBuffering(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onPlaying(Consumer<Void> listener) {}

        @Override
        public boolean removeOnPlaying(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onPaused(Consumer<Void> listener) {}

        @Override
        public boolean removeOnPaused(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onStopped(Consumer<Void> listener) {}

        @Override
        public boolean removeOnStopped(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onFinished(Consumer<Void> listener) {}

        @Override
        public boolean removeOnFinished(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onTimeChanged(Consumer<TimeChangedEvent> listener) {}

        @Override
        public boolean removeOnTimeChanged(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onVolumeChanged(Consumer<VolumeChangedEvent> listener) {}

        @Override
        public boolean removeOnVolumeChanged(Consumer<?> listener) {
            return false;
        }

        @Override
        public void onError(Consumer<Void> listener) {}

        @Override
        public boolean removeOnError(Consumer<?> listener) {
            return false;
        }
        
    }
    
}
