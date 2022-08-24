package com.igrium.videolib.render;

import java.nio.ByteBuffer;

import com.mojang.blaze3d.systems.RenderSystem;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

/**
 * A video surface that draws to an OpenGL texture
 */
public class OpenGLVideoSurface extends VideoSurface {
    
    private final GlBufferFormatCallback bufferFormatCallback = new GlBufferFormatCallback();
    private final GlBufferRenderCallback bufferRenderCallback = new GlBufferRenderCallback();

    protected final CallbackVideoSurface baseVideoSurface;
    
    protected final VideoTexture texture = new VideoTexture();

    public OpenGLVideoSurface(VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);
        baseVideoSurface = new CallbackVideoSurface(
                bufferFormatCallback,
                bufferRenderCallback,
                true,
                videoSurfaceAdapter);
    }

    public OpenGLVideoSurface() {
        this(VideoSurfaceAdapters.getVideoSurfaceAdapter());
    }

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        baseVideoSurface.attach(mediaPlayer);
    }

    /**
     * Get the texture this surface uses.
     * @return MC Texture
     */
    public VideoTexture getTexture() {
        return texture;
    }
    
    private class GlBufferFormatCallback implements BufferFormatCallback {

        private int sourceWidth;
        private int sourceHeight;

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            this.sourceWidth = sourceWidth;
            this.sourceHeight = sourceHeight;
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void allocatedBuffers(ByteBuffer[] buffers) {
            texture.allocate(sourceWidth, sourceHeight);
        }
    }

    private class GlBufferRenderCallback implements RenderCallback {

        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            texture.getBuffer().put(nativeBuffers[0]);
            RenderSystem.recordRenderCall(texture::upload);
        }
        
    }
}
