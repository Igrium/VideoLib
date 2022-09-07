package com.igrium.videolib.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/**
 * Various functions related to server-client networking.
 */
public final class VideoLibNetworking {
    public static enum InstallStatus {
        NOT_INSTALLED(false),
        MISSING_NATIVES(false),
        INSTALLED(true);

        private final boolean canPlay;

        InstallStatus(boolean canPlay) {
            this.canPlay = canPlay;
        }

        public boolean canPlay() {
            return canPlay;
        }
    }

    public static final Identifier SYNC_VIDEOLIB_STATUS = new Identifier("videolib", "sync_status");
    public static final Identifier PLAYBACK_COMMAND = new Identifier("videolib", "playback_command");

    /**
     * An instruction that can be sent from the server to the client to control fullscreen video playback.
     */
    public static record PlaybackCommand(boolean shouldStop, boolean isPaused, Optional<Identifier> id,
            Optional<URL> url, Optional<Long> timeMillis) {
        
        public void toByteBuf(PacketByteBuf buf) {
            buf.writeBoolean(shouldStop);
            buf.writeBoolean(isPaused);
            buf.writeOptional(id, (b, id) -> b.writeIdentifier(id));
            buf.writeOptional(url, (b, url) -> b.writeString(url.toString()));
            buf.writeOptional(timeMillis, (b, time) -> b.writeLong(time));
        }

        public static PlaybackCommand fromByteBuf(PacketByteBuf buf) {
            boolean shouldStop = buf.readBoolean();
            boolean isPaused = buf.readBoolean();
            Optional<Identifier> id = buf.readOptional(b -> b.readIdentifier());

            // Read URL manually to handle exception.
            Optional<URL> url;
            if (buf.readBoolean()) {
                try {
                    url = Optional.of(new URL(buf.readString()));
                } catch (MalformedURLException e) {
                    LogManager.getLogger().error("Illegal URL recieved in packet. " + e.getMessage());
                    url = Optional.empty();
                }
            } else {
                url = Optional.empty();
            }

            Optional<Long> timeMillis = buf.readOptional(b -> b.readLong());

            return new PlaybackCommand(shouldStop, isPaused, id, url, timeMillis);
        }

        /**
         * Whether this command should prompt the client to open the video playback screen.
         */
        public boolean shouldOpenScreen() {
            return id().isPresent() || url.isPresent();
        }
    }
}
