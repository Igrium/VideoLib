package com.igrium.videolib.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.texture.NativeImage;

@Mixin(NativeImage.class)
public interface NativeImageAccessor {

    @Accessor("pointer")
    public long getPointer();
}
