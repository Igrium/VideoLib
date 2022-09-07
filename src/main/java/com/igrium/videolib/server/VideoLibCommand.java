package com.igrium.videolib.server;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.igrium.videolib.server.VideoLibNetworking.InstallStatus;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;


public final class VideoLibCommand  {
    private VideoLibCommand() {};

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralCommandNode<ServerCommandSource> root = literal("videolib")
                .requires(source -> source.hasPermissionLevel(2)).build();
        
        LiteralCommandNode<ServerCommandSource> status = literal("status").then(
                argument("player", EntityArgumentType.player()).executes(
                        context -> getStatus(EntityArgumentType.getPlayer(context, "player"), context.getSource())))
                .build();
        root.addChild(status);

        dispatcher.getRoot().addChild(root);
    }

    private static int getStatus(ServerPlayerEntity player, ServerCommandSource source) throws CommandSyntaxException {
        InstallStatus status = VideoLibServer.getInstallStatus(player);
        if (status == InstallStatus.NOT_INSTALLED) {
            source.sendFeedback(new TranslatableText("commands.videolib.status.not_installed", player.getDisplayName())
                    .formatted(Formatting.RED), false);
            return 0;
        } else if (status == InstallStatus.MISSING_NATIVES) {
            source.sendFeedback(new TranslatableText("commands.videolib.status.missing_natives", player.getDisplayName())
                    .formatted(Formatting.YELLOW), false);
            return 1;
        } else if (status == InstallStatus.INSTALLED) {
            source.sendFeedback(new TranslatableText("commands.videolib.status.installed", player.getDisplayName())
                    .formatted(Formatting.GREEN), false);
            return 2;
        } else {
            throw new SimpleCommandExceptionType(new LiteralText("Unknown status type: "+status)).create();
        }
    }
}
