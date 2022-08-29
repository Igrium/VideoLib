package com.igrium.videolib.api.playback;

import java.util.function.Consumer;

/**
 * An interface for registering and un-registering callbacks pertaining to video
 * playback. All callbacks will be triggered on the Minecraft client thread.
 */
public interface VideoEvents {

    /**
     * An event listener that removes itself after its invoked.
     */
    public static class SingleFireEvent<T> implements Consumer<T> {

        private Consumer<Consumer<?>> removeFunction;
        private Consumer<T> acceptFunction;

        public SingleFireEvent(Consumer<T> acceptFunction, Consumer<Consumer<?>> removeFunction) {
            this.acceptFunction = acceptFunction;
            this.removeFunction = removeFunction;
        }

        @Override
        public void accept(T t) {
            acceptFunction.accept(t);
            removeFunction.accept(this);
        }
        
    }

    public static record BufferingEvent(float percentage) {};
    public static record TimeChangedEvent(long newTime) {};
    public static record VolumeChangedEvent(float newVolume) {};

    void onOpening(Consumer<Void> listener);
    boolean removeOnOpening(Consumer<?> listener);

    default void onceOpening(Consumer<Void> listener) {
        onOpening(new SingleFireEvent<>(listener, this::removeOnOpening));
    }

    void onBuffering(Consumer<BufferingEvent> listener);
    boolean removeOnBuffering(Consumer<?> listener);

    default void onceBuffering(Consumer<BufferingEvent> listener) {
        onBuffering(new SingleFireEvent<>(listener, this::removeOnBuffering));
    }

    void onPlaying(Consumer<Void> listener);
    boolean removeOnPlaying(Consumer<?> listener);

    default void oncePlaying(Consumer<Void> listener) {
        onOpening(new SingleFireEvent<>(listener, this::removeOnPlaying));
    }

    void onPaused(Consumer<Void> listener);
    boolean removeOnPaused(Consumer<?> listener);

    default void oncePaused(Consumer<Void> listener) {
        onOpening(new SingleFireEvent<>(listener, this::removeOnPaused));
    }

    void onStopped(Consumer<Void> listener);
    boolean removeOnStopped(Consumer<?> listener);

    default void onceStopped(Consumer<Void> listener) {
        onOpening(new SingleFireEvent<>(listener, this::removeOnStopped));
    }

    void onFinished(Consumer<Void> listener);
    boolean removeOnFinished(Consumer<?> listener);

    default void onceFinished(Consumer<Void> listener) {
        onFinished(new SingleFireEvent<>(listener, this::removeOnFinished));
    }

    void onTimeChanged(Consumer<TimeChangedEvent> listener);
    boolean removeOnTimeChanged(Consumer<?> listener);

    default void onceTimeChanged(Consumer<TimeChangedEvent> listener) {
        onTimeChanged(new SingleFireEvent<>(listener, this::removeOnTimeChanged));
    }

    void onVolumeChanged(Consumer<VolumeChangedEvent> listener);
    boolean removeOnVolumeChanged(Consumer<?> listener);

    default void onceVolumeChanged(Consumer<VolumeChangedEvent> listener) {
        onVolumeChanged(new SingleFireEvent<>(listener, this::removeOnVolumeChanged));
    }

    void onError(Consumer<Void> listener);
    boolean removeOnError(Consumer<?> listener);

    default void onceError(Consumer<Void> listener) {
        onOpening(new SingleFireEvent<>(listener, this::removeOnError));
    }
}
