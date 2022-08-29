package com.igrium.videolib.vlc;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public final class VLCUtils {
    private VLCUtils() {};

    // https://wiki.videolan.org/Protocols/
    public static final Set<String> allowedProtocols = ImmutableSet.of(
            "ftp",
            "ftps",
            "ftpes",
            "http",
            "https",
            "mms",
            "mmsh",
            "rtp",
            "rtcp",
            "rtmp",
            "rtsp",
            "sap",
            "sdp",
            "sip",
            "tcp",
            "udp",
            "cd",
            "fake",
            "file",
            "vcd");
}
