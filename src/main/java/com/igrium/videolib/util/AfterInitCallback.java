package com.igrium.videolib.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

/**
 * Called after the client (and all mods) have finished initializing, but before the splash screen is displayed.
 */
public interface AfterInitCallback {
    Event<AfterInitCallback> EVENT = EventFactory.createArrayBacked(AfterInitCallback.class,
        listeners -> (client) -> {
            for (AfterInitCallback listener : listeners) {
                listener.afterInit(client);
            }
        });

    void afterInit(MinecraftClient client);
}
