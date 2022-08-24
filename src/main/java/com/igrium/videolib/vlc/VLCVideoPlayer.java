package com.igrium.videolib.vlc;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import javax.annotation.Nullable;

import com.igrium.videolib.api.VideoPlayer;
import com.igrium.videolib.api.playback.ControlsInterface;
import com.igrium.videolib.api.playback.MediaInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VLCVideoPlayer implements VideoPlayer {
    protected final Identifier id;
    protected final VLCVideoManager manager;

    protected EmbeddedMediaPlayer mediaPlayer;
    protected OpenGLVideoSurface surface;

    protected VLCMediaInterface mediaInterface = new VLCMediaInterface();
    protected VLCControlsInterface controlsInterface = new VLCControlsInterface();

    @Nullable
    private VLCVideoHandle currentMedia;
    

    protected VLCVideoPlayer(Identifier id, VLCVideoManager manager) {
        this.id = id;
        this.manager = manager;
    }

    protected void init() {
        if (mediaPlayer != null) {
            throw new IllegalStateException("Media player has already been initialized.");
        }

        mediaPlayer = manager.getFactory().mediaPlayers().newEmbeddedMediaPlayer();
        surface = new OpenGLVideoSurface();
        mediaPlayer.videoSurface().set(surface);

        MinecraftClient.getInstance().getTextureManager().registerTexture(getTexture(), surface.getTexture());
    }

    public final Identifier getId() {
        return id;
    }

    public Identifier getTexture() {
        return getTextureId(getId());
    }

    /**
     * Generate a texture identifier from a video player identifier.
     * @param id Video player ID.
     * @return Texture ID.
     */
    public static Identifier getTextureId(Identifier id) {
        String namespace = id.getNamespace();
        String path = "videoplayers/"+id;
        return new Identifier(namespace, path);
    }

    @Override
    public void close() {
        surface.texture.close();        
    }

    @Override
    public MediaInterface<VLCVideoHandle> getMediaInterface() {
        return mediaInterface;
    }

    @Override
    public ControlsInterface getControlsInterface() {
        return controlsInterface;
    }

    public class VLCMediaInterface implements MediaInterface<VLCVideoHandle> {

        @Override
        public boolean load(VLCVideoHandle handle) {
            return mediaPlayer.media().prepare(handle.getMrl());
        }

        @Override
        public boolean play(VLCVideoHandle handle) {
            currentMedia = handle;
            return false;
        }

        @Override
        public boolean hasMedia() {
            return currentMedia != null;
        }

        @Override
        public Optional<VLCVideoHandle> currentMedia() {
            return Optional.ofNullable(currentMedia);
        }

        @Override
        public VLCVideoHandle getHandle(Identifier id) {
            return null;
        }

        @Override
        public VLCVideoHandle getHandle(URI uri) throws MalformedURLException {
            return new VLCUtils.VLCUrlHandle(uri);
        }
    }

    public class VLCControlsInterface implements ControlsInterface {

        @Override
        public void play() {
            mediaPlayer.controls().play();           
        }

        @Override
        public void stop() {
            mediaPlayer.controls().stop();
            currentMedia = null;
        }

        @Override
        public void setPause(boolean pause) {
            mediaPlayer.controls().setPause(pause);
        }

        @Override
        public void setTime(long time) {
            mediaPlayer.controls().setTime(time);
        }

        @Override
        public long getTime() {
            return mediaPlayer.status().time();
        }

        @Override
        public long getLength() {
            return mediaPlayer.status().length();
        }

        @Override
        public void setRepeat(boolean repeat) {
            mediaPlayer.controls().setRepeat(repeat);
        }

        @Override
        public boolean repeat() {
            return mediaPlayer.controls().getRepeat();
        }
        
    }
}
