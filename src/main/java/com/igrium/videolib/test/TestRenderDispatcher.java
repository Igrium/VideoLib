package com.igrium.videolib.test;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TestRenderDispatcher {

    public final Set<TestVideoPlayer> players = new HashSet<>();

    public void render(WorldRenderContext context) {
        MatrixStack matrices = context.matrixStack();
        matrices.push();

        Vec3d cameraPos = context.camera().getPos();

        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        for (TestVideoPlayer player : players) {
            int light = context.world().getLightLevel(new BlockPos(player.getPos()));
            player.render(context.tickDelta(), matrices, light, context.consumers());
        }

        matrices.pop();
    }
}
