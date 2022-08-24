package com.igrium.videolib.test;

import com.igrium.videolib.VideoLibClient;
import com.igrium.videolib.api.MediaManager;
import com.igrium.videolib.api.VideoManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;;


public final class AddVideoPlayerCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        
        LiteralCommandNode<FabricClientCommandSource> spawnVideoPlayer = literal("spawnVideoPlayer").executes(context -> {
            VideoManager videoManager = VideoLibClient.getInstance().getVideoManager();

            TestVideoPlayer videoPlayer = new TestVideoPlayer();
            videoPlayer.setPos(context.getSource().getPosition());

            
            mediaManager.setup();
            mediaManager.getMediaPlayer().media()
                    .play("file:///F:/Documents/Programming/tests/vlcj_test/app/src/main/resources/crash_test.mp4");

            context.getSource().sendFeedback(new LiteralText("Spawned video player at "+videoPlayer.getPos()));

            VideoLibClient.getInstance().getTestRenderDispatcher().players.add(videoPlayer);

            return 1;
        }).build();
        dispatcher.getRoot().addChild(spawnVideoPlayer);

        LiteralCommandNode<FabricClientCommandSource> updateVideoPlayer = literal("updateVideoPlayer").executes(context -> {
            MediaManager mediaManager = VideoLibClient.getInstance().getMediaManager();
            mediaManager.getMediaPlayer().media()
                    .play("file:///F:/Documents/Programming/tests/vlcj_test/app/src/main/resources/creeper.mp4");
            
            context.getSource().sendFeedback(new LiteralText("Updated video."));
            return 1;
        }).build();
        dispatcher.getRoot().addChild(updateVideoPlayer);
    }
    
}
