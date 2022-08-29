package com.igrium.videolib.demo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

/**
 * Adds a simple client command that demonstrates VideoLib's capabilities.
 */
public class VideoLibDemo implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PlayVideoCommand.register(ClientCommandManager.DISPATCHER);
    }
    
}
