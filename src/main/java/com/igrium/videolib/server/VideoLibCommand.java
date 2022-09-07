package com.igrium.videolib.server;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

import com.igrium.videolib.server.VideoLibNetworking.InstallStatus;
import com.igrium.videolib.server.VideoLibNetworking.PlaybackCommand;
import com.igrium.videolib.util.UriArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;


public final class VideoLibCommand  {
    private VideoLibCommand() {};

    public static record SimpleVideoHandle(Optional<Identifier> id, Optional<URL> url) {
        public SimpleVideoHandle(Identifier id) {
            this(Optional.of(id), Optional.empty());
        }
        public SimpleVideoHandle(URL url) {
            this(Optional.empty(), Optional.of(url));
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralCommandNode<ServerCommandSource> root = literal("videolib")
                .requires(source -> source.hasPermissionLevel(2)).build();
        
        LiteralCommandNode<ServerCommandSource> status = literal("status").then(
                argument("player", EntityArgumentType.player()).executes(
                        context -> getStatus(EntityArgumentType.getPlayer(context, "player"), context.getSource())))
                .build();
        root.addChild(status);

        LiteralCommandNode<ServerCommandSource> play = literal("play").then(
                argument("url", UriArgumentType.uri()).then(
                    argument("targets", EntityArgumentType.players()).executes(context -> {
                        URL url;
                        try {
                            url = UriArgumentType.getUri(context, "url").toURL();
                        } catch (MalformedURLException e) {
                            throw new SimpleCommandExceptionType(Text.of(e.getMessage())).create();
                        }
                        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                        return start(players, new SimpleVideoHandle(url), context.getSource());
                    })
                )
            ).then(
                argument("id", IdentifierArgumentType.identifier()).then(
                    argument("targets", EntityArgumentType.players()).executes(context -> {
                        Identifier id = IdentifierArgumentType.getIdentifier(context, "id");
                        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                        return start(players, new SimpleVideoHandle(id), context.getSource());
                    })
                )
            ).build();
        root.addChild(play);

        LiteralCommandNode<ServerCommandSource> setPause = literal("setPause").then(
            argument("pause", BoolArgumentType.bool()).then(
                argument("targets", EntityArgumentType.players()).executes(context -> {
                    boolean shouldPause = BoolArgumentType.getBool(context, "pause");
                    PlaybackCommand command = new PlaybackCommand(false, shouldPause, Optional.empty(),
                            Optional.empty(), Optional.empty());
                    Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                    return sendPlaybackCommand(players, command, context.getSource());
                })
            )
        ).build();
        root.addChild(setPause);

        LiteralCommandNode<ServerCommandSource> stop = literal("stop").then(
            argument("targets", EntityArgumentType.players()).executes(context -> {
                PlaybackCommand command = new PlaybackCommand(true, false, Optional.empty(), Optional.empty(), Optional.empty());
                Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                return sendPlaybackCommand(players, command, context.getSource());
            })
        ).build();
        root.addChild(stop);


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

    private static int start(Collection<ServerPlayerEntity> players, SimpleVideoHandle handle, ServerCommandSource source) throws CommandSyntaxException {
        PlaybackCommand command = new PlaybackCommand(false, false, handle.id(), handle.url(), Optional.empty());
        return sendPlaybackCommand(players, command, source);
    }

    private static int sendPlaybackCommand(Collection<ServerPlayerEntity> players, PlaybackCommand playbackCommand,
            ServerCommandSource source) throws CommandSyntaxException {
        int success = 0;
        int fail = 0;
        ServerPlayerEntity lastFail = null;

        for (ServerPlayerEntity player : players) {
            InstallStatus status = VideoLibServer.getInstallStatus(player);
            if (!status.canPlay()) {
                fail++;
                lastFail = player;
                continue;
            }

            VideoLibServer.sendPlaybackCommand(player, playbackCommand);
            success++;
        }

        MutableText response = new LiteralText("");
        if (success > 0) {
            response.append(new TranslatableText("commands.videolib.send_command.success", success));
            if (fail > 0) response.append(" ");
        }
        if (fail == 1) {
            response.append(
                    new TranslatableText("commands.videolib.send_command.fail_single", lastFail.getDisplayName())
                            .formatted(Formatting.RED));
        } else if (fail > 0) {
            response.append(
                    new TranslatableText("commands.videolib.send_command.fail", fail).formatted(Formatting.RED));
        }

        source.sendFeedback(response, true);
        return success;
    }
}
