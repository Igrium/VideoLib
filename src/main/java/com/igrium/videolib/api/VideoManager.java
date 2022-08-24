package com.igrium.videolib.api;

import net.minecraft.util.Identifier;

/**
 * Responsible for loading and preparing video players.
 */
public interface VideoManager extends AutoCloseable {

    /**
     * Was this video manager able to properly load it's native dependencies?
     * @return If we have natives.
     */
    public boolean hasNatives();
    
    /**
     * Get a video player by its identifier.
     * 
     * @param id Identifier to use.
     * @return Corresponding video player, or <code>null</code> if no video player
     *         by this ID exists.
     */
    public VideoPlayer getPlayer(Identifier id);

    /**
     * Get a video player by its identifier, or create it if it does not exist.
     * 
     * @param id Identifier to use.
     * @return The video player.
     */
    public VideoPlayer getOrCreate(Identifier id);

    /**
     * Close a video player and remove it from this manager's pool.
     * 
     * @param id Video player ID.
     * @return If this video player was found.
     */
    public boolean closePlayer(Identifier id);
}
