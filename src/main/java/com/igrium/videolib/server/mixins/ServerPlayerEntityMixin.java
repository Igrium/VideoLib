package com.igrium.videolib.server.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.igrium.videolib.server.VideoLibNetworking.InstallStatus;
import com.igrium.videolib.server.VideoLibStatusHolder;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements VideoLibStatusHolder {

    private InstallStatus installStatus = InstallStatus.NOT_INSTALLED;

    @Override
    public InstallStatus getVideoLibStatus() {
        return installStatus;
    }

    @Override
    public void setVideoLibStatus(InstallStatus status) {
        this.installStatus = status;
    }

    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At("TAIL"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean isAlive, CallbackInfo ci) {
        setVideoLibStatus(((VideoLibStatusHolder) oldPlayer).getVideoLibStatus());
    }
    
}
