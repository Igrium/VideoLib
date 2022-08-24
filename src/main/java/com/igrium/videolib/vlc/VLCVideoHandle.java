package com.igrium.videolib.vlc;

import java.io.IOException;
import java.io.InputStream;

import com.igrium.videolib.api.VideoHandle;

public interface VLCVideoHandle extends VideoHandle {
    String getMrl();

    public static VLCVideoHandle fromMRL(String mrl) {
        return new VLCVideoHandle() {

            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public InputStream openStream() throws IOException {
                return null;
            }

            @Override
            public String getMrl() {
                return mrl;
            }
            
        };
    }
}
