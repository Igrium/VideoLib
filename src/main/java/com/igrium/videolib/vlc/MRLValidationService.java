package com.igrium.videolib.vlc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

/**
 * A "hack" which uses a hidden media player to validate MRLs
 */
public final class MRLValidationService {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Abuse an audio player to make connection with MRL.
    private final AudioPlayerComponent mediaPlayer = new AudioPlayerComponent();

    public CompletableFuture<Boolean> validate(String mrl) {
        return CompletableFuture.supplyAsync(() -> validateImpl(mrl), executor);
    }

    private boolean validateImpl(String mrl) {
        return mediaPlayer.mediaPlayer().media().prepare(mrl);
    }
    
}
