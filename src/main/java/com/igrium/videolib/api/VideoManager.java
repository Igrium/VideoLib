package com.igrium.videolib.api;

import java.util.Collection;

import com.igrium.videolib.util.MissingNativesException;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

/**
 * Responsible for loading and preparing video players.
 */
public interface VideoManager extends AutoCloseable {

    public static interface VideoManagerFactory {

        /**
         * Create a video manager.
         * @return The newly-created video manager.
         * @throws MissingNativesException If the native dependencies cannot be loaded.
         */
        public VideoManager create() throws MissingNativesException;
    }
    
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

    /**
     * Get a resource reload listener responsible for loading video files.
     * @return Reload listener.
     */
    public IdentifiableResourceReloadListener getReloadListener();

    /**
     * Get the extensions that this implementation supports.
     * @return A collection of extensions, excluding the period. ('mp4', NOT '.mp4')
     */
    public Collection<String> supportedExtensions();

    /**
     * Get a video handle factory that will create handles compatible with this
     * video manager.
     * 
     * @return The video handle factory.
     */
    public VideoHandleFactory getVideoHandleFactory();
}
