package com.igrium.videolib.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.igrium.videolib.util.AfterInitCallback;

import net.minecraft.client.MinecraftClient;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashOverlay;init(Lnet/minecraft/client/MinecraftClient;)V"))
    void afterInit(CallbackInfo ci) {
        AfterInitCallback.EVENT.invoker().afterInit((MinecraftClient) (Object) this);
    }
}
