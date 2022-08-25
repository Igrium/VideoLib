package com.igrium.videolib.test;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.igrium.videolib.VideoLib;
import com.igrium.videolib.VideoLibClient;
import com.igrium.videolib.api.VideoPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;;


public final class AddVideoPlayerCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        
        LiteralCommandNode<FabricClientCommandSource> spawnVideoPlayer = literal("spawnVideoPlayer").executes(context -> {
            TestVideoPlayer videoPlayer = new TestVideoPlayer();
            videoPlayer.setPos(context.getSource().getPosition());

            VideoPlayer player = VideoLib.getInstance().getDefaultPlayer();
            videoPlayer.setTexture(player.getTexture());
            
            player.getMediaInterface().play(new Identifier("videolib", "videos/camera.mp4"));

            VideoLibClient.getInstance().getTestRenderDispatcher().players.add(videoPlayer);

            return 1;
        }).build();
        dispatcher.getRoot().addChild(spawnVideoPlayer);

        LiteralCommandNode<FabricClientCommandSource> updateVideoPlayer = literal("updateVideoPlayer").executes(context -> {
            VideoPlayer player = VideoLib.getInstance().getDefaultPlayer();
            try {
                player.getMediaInterface().play("file:///F:/Documents/Programming/tests/vlcj_test/app/src/main/resources/creeper.mp4");
            } catch (MalformedURLException | URISyntaxException e) {
                throw new SimpleCommandExceptionType(Text.of(e.getMessage())).create();
            }
            
            context.getSource().sendFeedback(new LiteralText("Updated video."));
            return 1;
        }).build();
        dispatcher.getRoot().addChild(updateVideoPlayer);
    }
    
}
