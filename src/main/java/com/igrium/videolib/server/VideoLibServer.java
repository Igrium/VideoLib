package com.igrium.videolib.server;

import org.apache.logging.log4j.LogManager;

import com.igrium.videolib.server.VideoLibNetworking.InstallStatus;
import com.igrium.videolib.server.VideoLibNetworking.PlaybackCommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Optional server-side functions regarding VideoLib
 */
public class VideoLibServer implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(VideoLibNetworking.SYNC_VIDEOLIB_STATUS,
            (server, player, handler, buf, response) -> {
                InstallStatus status = buf.readEnumConstant(InstallStatus.class);
                setInstallStatus(player, status);
                LogManager.getLogger().info("VideoLib Status for {}: {}", player.getEntityName(), status);
            });
        
        CommandRegistrationCallback.EVENT.register(VideoLibCommand::register);
    }

    /**
     * Get the VideoLib install status of a player.
     * @param player Target player.
     * @return VideoLib install status.
     */
    public static InstallStatus getInstallStatus(ServerPlayerEntity player) {
        return ((VideoLibStatusHolder) player).getVideoLibStatus();
    }

    private static void setInstallStatus(ServerPlayerEntity player, InstallStatus status) {
        ((VideoLibStatusHolder) player).setVideoLibStatus(status);
    }

    /**
     * Send a VideoLib playback command to a player on the server.
     * @param player Player to send to.
     * @param command Command to send.
     * @throws IllegalStateException If the player does not have VideoLib installed.
     */
    public static void sendPlaybackCommand(ServerPlayerEntity player, PlaybackCommand command) throws IllegalStateException {
        InstallStatus status = getInstallStatus(player);
        if (status == InstallStatus.NOT_INSTALLED) {
            throw new IllegalStateException(player.getEntityName() + " does not have VideoLib installed!");
        } else if (status == InstallStatus.MISSING_NATIVES) {
            LogManager.getLogger().warn("{}'s installation of VideoLib is missing natives!", player.getEntityName());
        }

        PacketByteBuf buf = PacketByteBufs.create();
        command.toByteBuf(buf);
        ServerPlayNetworking.send(player, VideoLibNetworking.PLAYBACK_COMMAND, buf);
    }
}
