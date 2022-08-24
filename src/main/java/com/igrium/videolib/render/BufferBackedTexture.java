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

    public BufferBackedTexture(ByteBuffer buffer, int width, int height, int format) {
        setBuffer(buffer, width, height, format);
    }

    public BufferBackedTexture() {
        setBuffer(null, 0, 0, GL12.GL_BGRA);
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
    
    public synchronized void setBuffer(ByteBuffer buffer, int width, int height, int format) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.format = format;
        isPrepared = false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

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
