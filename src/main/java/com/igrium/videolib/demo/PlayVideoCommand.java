package com.igrium.videolib.demo;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

import java.net.MalformedURLException;
import java.net.URI;

import com.igrium.videolib.VideoLib;
import com.igrium.videolib.api.VideoHandle;
import com.igrium.videolib.api.VideoPlayer;
import com.igrium.videolib.render.VideoScreen;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class PlayVideoCommand {
    private PlayVideoCommand() {};
    private static VideoLib videoLib = VideoLib.getInstance();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("playvideo").then(
            argument("id", IdentifierArgumentType.identifier()).executes(context -> {
                Identifier id = context.getArgument("id", Identifier.class);
                VideoHandle handle = videoLib.getHandleFactory().getVideoHandle(id);
                return play(handle, context.getSource()) ? 1 : 0;
            })
        ).then(
            argument("url", UriArgumentType.uri()).executes(context -> {
                URI uri = UriArgumentType.getUri(context, "url");
                VideoHandle handle;
                try {
                    handle = videoLib.getHandleFactory().getVideoHandle(uri.toURL());
                } catch (MalformedURLException e) {
                    throw new SimpleCommandExceptionType(Text.of(e.getMessage())).create();
                }
                return play(handle, context.getSource()) ? 1 : 0;
            })
        ));
    }

    public static boolean play(VideoHandle handle, FabricClientCommandSource source) {
        VideoPlayer player = videoLib.getDefaultPlayer();
        boolean success = new VideoScreen(player).playAndShow(handle);
        if (!success) {
            source.sendError(new LiteralText("Unable to open "+handle));
        }
        return success;
    }
}
