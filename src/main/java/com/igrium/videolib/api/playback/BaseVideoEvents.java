package com.igrium.videolib.api.playback;

import java.util.function.Consumer;

import com.igrium.videolib.util.EventEmitter;

import net.minecraft.client.MinecraftClient;

/**
 * A base implementation of VideoEvents using EventEmitters.
 */
public abstract class BaseVideoEvents implements VideoEvents {

    private MinecraftClient client = MinecraftClient.getInstance();

    protected final EventEmitter<Void> opening = new EventEmitter<>(client);
    protected final EventEmitter<BufferingEvent> buffering = new EventEmitter<>(client);
    protected final EventEmitter<Void> playing = new EventEmitter<>(client);
    protected final EventEmitter<Void> paused = new EventEmitter<>(client);
    protected final EventEmitter<Void> stopped = new EventEmitter<>(client);
    protected final EventEmitter<Void> finished = new EventEmitter<>(client);
    protected final EventEmitter<TimeChangedEvent> timeChanged = new EventEmitter<>(client);
    protected final EventEmitter<VolumeChangedEvent> volumeChanged = new EventEmitter<>(client);
    protected final EventEmitter<Void> error = new EventEmitter<>(client);

    // API EVENTS

    @Override
    public void onOpening(Consumer<Void> listener) {
        opening.addListener(listener);
    }

    @Override
    public boolean removeOnOpening(Consumer<?> listener) {
        return opening.removeListener(listener);
    }

    protected void invokeOnOpening() {
        opening.invoke(null);
    }

    @Override
    public void onBuffering(Consumer<BufferingEvent> listener) {
        buffering.addListener(listener);
    }

    @Override
    public boolean removeOnBuffering(Consumer<?> listener) {
        return buffering.removeListener(listener);
    }

    protected void invokeOnBuffering(float percentage) {
        buffering.invoke(new BufferingEvent(percentage));
    }

    @Override
    public void onPlaying(Consumer<Void> listener) {
        playing.addListener(listener);
    }

    @Override
    public boolean removeOnPlaying(Consumer<?> listener) {
        return playing.removeListener(listener);
    }

    protected void invokeOnPlaying() {
        playing.invoke(null);
    }

    @Override
    public void onPaused(Consumer<Void> listener) {
        paused.addListener(listener);
    }

    @Override
    public boolean removeOnPaused(Consumer<?> listener) {
        return paused.removeListener(listener);
    }

    protected void invokeOnPaused() {
        paused.invoke(null);
    }

    @Override
    public void onStopped(Consumer<Void> listener) {
        stopped.addListener(listener);
    }

    @Override
    public boolean removeOnStopped(Consumer<?> listener) {
        return stopped.removeListener(listener);
    }

    protected void invokeOnStopped() {
        stopped.invoke(null);
    }

    @Override
    public void onFinished(Consumer<Void> listener) {
        finished.addListener(listener);
    }

    @Override
    public boolean removeOnFinished(Consumer<?> listener) {
        return finished.removeListener(listener);
    }

    protected void invokeOnFinished() {
        finished.invoke(null);
    }

    @Override
    public void onTimeChanged(Consumer<TimeChangedEvent> listener) {
        timeChanged.addListener(listener);
    }

    @Override
    public boolean removeOnTimeChanged(Consumer<?> listener) {
        return timeChanged.removeListener(listener);
    }

    protected void invokeOnTimeChanged(long time) {
        timeChanged.invoke(new TimeChangedEvent(time));
    }

    @Override
    public void onVolumeChanged(Consumer<VolumeChangedEvent> listener) {
        volumeChanged.addListener(listener);
    }

    @Override
    public boolean removeOnVolumeChanged(Consumer<?> listener) {
        return volumeChanged.removeListener(listener);
    }

    protected void invokeOnVolumeChanged(float volume) {
        volumeChanged.invoke(new VolumeChangedEvent(volume));
    }

    @Override
    public void onError(Consumer<Void> listener) {
        error.addListener(listener);
    }

    @Override
    public boolean removeOnError(Consumer<?> listener) {
        return error.removeListener(listener);
    }

    protected void invokeOnError() {
        error.invoke(null);
    }
}
