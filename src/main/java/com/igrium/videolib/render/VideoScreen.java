package com.igrium.videolib.render;

import org.joml.Matrix4f;

import com.igrium.videolib.VideoLib;
import com.igrium.videolib.api.VideoHandle;
import com.igrium.videolib.api.VideoPlayer;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

/**
 * Renders a video player in a traditional fullscreen interface.
 */
public class VideoScreen extends Screen {

    protected record SimpleQuad(float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1){
        public SimpleQuad(float x0, float y0, float x1, float y1) {
            this(x0, y0, x1, y1, 0, 0, 1, 1);
        }

        public void render(Identifier texture, DrawContext context) {
            // RenderSystem.setShaderTexture(0, texture);
            // RenderSystem.setShader(GameRenderer::getPositionTexProgram);

            // Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
            // BufferBuilder buffer = Tessellator.getInstance().getBuffer();

            // buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            // buffer.vertex(matrix, x0(), y1(), 0).texture(u0(), v1()).next();
            // buffer.vertex(matrix, x1(), y1(), 0).texture(u1(), v1()).next();
            // buffer.vertex(matrix, x1(), y0(), 0).texture(u1(), v0()).next();
            // buffer.vertex(matrix, x0(), y0(), 0).texture(u0(), v0()).next();

            // BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);

            Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

            bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, x0(), y0(), 0).texture(u0(), v0()).next();
            bufferBuilder.vertex(matrix4f, x0(), y1(), 0).texture(u0(), v1()).next();
            bufferBuilder.vertex(matrix4f, x1(), y1(), 0).texture(u1(), v1()).next();
            bufferBuilder.vertex(matrix4f, x1(), y0(), 0).texture(u1(), v0()).next();
            
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }
    }

    private VideoPlayer player;
    private int backgroundColor = ColorHelper.Argb.getArgb(255, 0, 0, 0);
    private boolean userClosable = true;

    private boolean isStopping = false;

    /**
     * Construct a fullscreen video screen.
     * @param player Video player to use.
     */
    public VideoScreen(VideoPlayer player) {
        super(Text.literal("Video Player"));
        this.player = player;
        player.getEvents().onceFinished(e -> {
            isStopping = true;
            close();
        });
    }

    /**
     * Get the video player that this screen uses.
     * @return Video player.
     */
    public VideoPlayer getPlayer() {
        return player;
    }

    /**
     * Load a video and display this screen once it starts playing.
     * @param handle The video handle.
     * @return Whether the video could be loaded.
     */
    public boolean playAndShow(VideoHandle handle) {
        boolean canPlay = player.getMediaInterface().play(handle);
        if (canPlay) {
            player.getEvents().oncePlaying(e -> MinecraftClient.getInstance().setScreen(this));
        }
        return canPlay;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setUserClosable(boolean userClosable) {
        this.userClosable = userClosable;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return userClosable;
    }

    /**
     * Construct a fullscreen video screen using the default player.
     */
    public VideoScreen() {
        this(VideoLib.getInstance().getDefaultPlayer());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderBackground(context, mouseX, mouseY, delta);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, player.getTexture());

        calculateQuad(player.getCodecInterface().getAspectRatio()).render(player.getTexture(), context);
    }

    // @Override
    // public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    //     super.render(matrices, mouseX, mouseY, delta);
    //     renderBackground(matrices);

    //     RenderSystem.setShader(GameRenderer::getPositionTexShader);
    //     RenderSystem.setShaderColor(1, 1, 1, 1);
    //     RenderSystem.setShaderTexture(0, player.getTexture());

    //     drawQuad(
    //             matrices.peek().getPositionMatrix(),
    //             calculateQuad(player.getCodecInterface().getAspectRatio()));
    // }
    
    // @Override
    // public void renderBackground(MatrixStack matrices, int vOffset) {
    //     fill(matrices, 0, 0, width, height, backgroundColor);
    // }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, backgroundColor);
    }

    @Override
    public void close() {
        if (!isStopping) player.getControlsInterface().stop();
        super.close();
    }



    /**
     * Find the quad that will best fit the image to the screen.
     * @param aspectRatio The video's aspect ratio.
     * @return The 
     */
    protected SimpleQuad calculateQuad(float aspectRatio) {
        float thisRatio = ((float) width) / height;

        float imageWidth;
        float imageHeight;
        if (aspectRatio > thisRatio) {
            imageWidth = width;
            imageHeight = width / aspectRatio;
        } else {
            imageHeight = height;
            imageWidth = height * aspectRatio;
        }

        float x = (width - imageWidth) / 2;
        float y = (height - imageHeight) / 2;
        
        return new SimpleQuad(x, y, x + imageWidth, y + imageHeight);
    }

}
