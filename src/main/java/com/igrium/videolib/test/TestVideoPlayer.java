package com.igrium.videolib.test;

import com.igrium.videolib.api.MediaManager;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

public class TestVideoPlayer {
    public static final Identifier TEXTURE = MediaManager.TEXTURE_ID;

    private Vec3d pos = new Vec3d(0, 0, 0);

    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    public Vec3d getPos() {
        return pos;
    }

    public void render(float tickDelta, MatrixStack matrices, int light, VertexConsumerProvider vertexConsumers) {
        matrices.push();
        matrices.translate(pos.x, pos.y, pos.z);

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        renderQuad(-1, 0, 1, 1, matrices.peek(), light, buffer);

        matrices.pop();
    }

    protected static void renderQuad(float minX, float minY, float maxX, float maxY, MatrixStack.Entry entry, int light, VertexConsumer buffer) {
        Matrix4f position = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        Vec3f[] verts = new Vec3f[] { new Vec3f(minX, minY, 0), new Vec3f(minX, maxY, 0), new Vec3f(maxX, maxY, 0),
            new Vec3f(maxX, minY, 0) };

        Vec3f normalVec = new Vec3f(-1, -1, 0);
        normalVec.transform(normal);

        float minU = 0;
        float minV = 1;
        float maxU = 1;
        float maxV = 0;

        for (int i = 0; i < 4; i++) {
            Vec3f vert = verts[i];
            Vector4f pos = new Vector4f(vert.getX(), vert.getY(), vert.getZ(), 1);
            pos.transform(position);

            float u = vert.getX() <= minX ? minU : maxU;
            float v = vert.getY() <= minY ? minV : maxV;

            buffer.vertex(pos.getX(), pos.getY(), pos.getZ(), 1, 1, 1, 1, u, v, OverlayTexture.DEFAULT_UV, 255,
                    normalVec.getX(), normalVec.getX(), normalVec.getZ());
        }

        // buffer.vertex(position, minX, maxY, 0).texture(minU, maxV).next();
        // buffer.vertex(position, maxX, maxY, 0).texture(maxU, maxV).next();
        // buffer.vertex(position, maxX, minY, 0).texture(maxU, minV).next();
        // buffer.vertex(position, minX, minY, 0).texture(minU, minV).next();
    }
}
