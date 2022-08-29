package com.igrium.videolib.vlc;

import com.igrium.videolib.api.playback.BaseVideoEvents;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

public class VLCEvents extends BaseVideoEvents implements MediaPlayerEventListener {

    // VLCJ EVENTS

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
        invokeOnOpening();
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {
        invokeOnBuffering(newCache / 100f);
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        invokeOnPlaying();
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        invokeOnPaused();
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        invokeOnStopped();
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        invokeOnFinished();
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        invokeOnTimeChanged(newTime);
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {  
    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType type, int id) {
    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType type, int id) {
    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, int id) {
    }

    @Override
    public void corked(MediaPlayer mediaPlayer, boolean corked) {
    }

    @Override
    public void muted(MediaPlayer mediaPlayer, boolean muted) {
    }

    @Override
    public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
        invokeOnVolumeChanged(volume);
    }

    @Override
    public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
    }

    @Override
    public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        invokeOnError();
    }

    @Override
    public void mediaPlayerReady(MediaPlayer mediaPlayer) {
    }
    
}
