package com.igrium.videolib.vlc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
    
    public static class VLCUrlHandle implements VLCVideoHandle {

        private final URI uri;

        public VLCUrlHandle(URI uri) throws MalformedURLException {
            this.uri = uri;
            if (!allowedProtocols.contains(uri.getScheme())) {
                throw new MalformedURLException("VLC does not support protocol: "+uri.getScheme());
            }
        }

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public InputStream openStream() throws IOException {
            URL url = uri.toURL();
            return url.openStream();
        }

        @Override
        public String getMrl() {
            return uri.toString();
        }

    }

    public static class VLCFileHandle implements VLCVideoHandle {

        private final File file;

        public VLCFileHandle(File file) {
            this.file = file;
        }

        @Override
        public boolean exists() {
            return file.exists();
        }

        @Override
        public InputStream openStream() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public String getMrl() {
            return file.toString();
        }
        
    }
}
