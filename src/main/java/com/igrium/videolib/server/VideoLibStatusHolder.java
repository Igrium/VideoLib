package com.igrium.videolib.server;

import com.igrium.videolib.server.VideoLibNetworking.InstallStatus;

/**
 * Stores the VideoLib install status for a client.
 */
public interface VideoLibStatusHolder {
    public InstallStatus getVideoLibStatus();
    public void setVideoLibStatus(InstallStatus status);
}
