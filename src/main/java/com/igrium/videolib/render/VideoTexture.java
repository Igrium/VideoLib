package com.igrium.videolib.render;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryUtil;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceManager;

/**
 * A texture that is backed by a video source.
 */
public class VideoTexture extends AbstractTexture {

    private ByteBuffer buffer;

    private int width;
    private int height;

    public VideoTexture(int width, int height) {
        allocate(width, height);
    }

    public VideoTexture() {
        allocate(0, 0);
    }

    /**
     * Get the buffer that this texture uses. Uses should be in a synchronised block
     * with this object in case another method tries to call allocate.
     * 
     * @return The buffer.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void load(ResourceManager manager) throws IOException {    

    }

    /**
     * Upload the current contents of this buffer to the GPU
     */
    public synchronized void upload() {
        RenderSystem.assertOnRenderThread();
        this.bindTexture();
        GL11.glDrawPixels(width, height, GL12.GL_BGR, GL11.GL_BYTE, buffer);
    }

    /**
     * Re-allocate this buffer.
     * @param width Width of the buffer.
     * @param height Height of the buffer.
     */
    public synchronized void allocate(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height must be greater than 0.");
        }

        if (buffer != null) {
            MemoryUtil.memFree(buffer);
        }

        this.width = width;
        this.height = height;

        buffer = MemoryUtil.memAlloc(width * height * 3);
    }

    @Override
    public synchronized void close() {
        if (buffer != null) {
            MemoryUtil.memFree(buffer);
        }
        super.close();
    }
    
}
