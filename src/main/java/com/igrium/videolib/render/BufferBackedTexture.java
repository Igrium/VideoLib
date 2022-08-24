package com.igrium.videolib.render;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryUtil;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;

/**
 * A texture that is backed by a mutable byte buffer.
 */
public class BufferBackedTexture extends AbstractTexture {
    private ByteBuffer buffer;

    private int width;
    private int height;
    private boolean isPrepared = false;

    private int format;
    Profiler profiler = MinecraftClient.getInstance().getProfiler();

    /**
     * Allocate a buffer and create a buffer-backed texture that will free the
     * buffer when it's closed.
     * 
     * @param width  Width of the image.
     * @param height Height of the image.
     * @param format OpenGL format of the pixel data.
     * @return The texture.
     */
    public static BufferBackedTexture create(int width, int height, int format) {
        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        BufferBackedTexture tex = new BufferBackedTexture(buffer, width, height, format) {
            @Override
            public void close() {
                super.close();
                MemoryUtil.memFree(buffer);
            }
        };

        return tex;
    }

    /**
     * Create a buffer backed texture
     * @param buffer Buffer to use. MAY NOT BE A HEAP BUFFER
     * @param width Width of the image.
     * @param height Height of the image.
     * @param format OpenGL format of the pixel data.
     */
    public BufferBackedTexture(ByteBuffer buffer, int width, int height, int format) {
        setBuffer(buffer, width, height, format);
    }

    /**
     * Create an instance without a backing buffer. Most methods will not work until
     * a buffer is set.
     */
    public BufferBackedTexture() {
        setBuffer(null, 0, 0, GL12.GL_BGRA);
    }

    /**
     * Get the buffer that this texture uses.
     * 
     * @return The buffer.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }
    
    /**
     * Set the buffer that this texture uses.
     * @param buffer Buffer to use. MAY NOT BE A HEAP BUFFER
     * @param width Width of the image.
     * @param height Height of the image.
     * @param format OpenGL format of the pixel data.
     */
    public synchronized void setBuffer(ByteBuffer buffer, int width, int height, int format) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.format = format;
        isPrepared = false;
    }

    /**
     * Get the width of the image.
     * @return Texture width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the image.
     * @return Texture height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Get the pixel format of the image.
     * @return OpenGL pixel format.
     */
    public int getFormat() {
        return format;
    }

    @Override
    public void load(ResourceManager manager) throws IOException {    

    }

    /**
     * Upload the current contents of this buffer to the GPU
     */
    public synchronized void upload() {
        RenderSystem.assertOnRenderThread();
        if (buffer == null) {
            throw new IllegalStateException("Buffer is not set.");
        }

        profiler.push("videoTexture");
        
        if (!isPrepared) {
            TextureUtil.prepareImage(getGlId(), width, height);
            isPrepared = true;
        }

        this.bindTexture();
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, format, GL11.GL_UNSIGNED_BYTE, buffer);
        profiler.pop();
    }

}   
