package com.igrium.videolib.api;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;

public class MediaManager {
    private MediaPlayerFactory factory;
    
    public MediaManager() {
        factory = new MediaPlayerFactory();

        // factory.mediaPlayers()
    }

    public MediaPlayerFactory getFactory() {
        return factory;
    }

    
}
