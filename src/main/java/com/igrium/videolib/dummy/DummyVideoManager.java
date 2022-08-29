package com.igrium.videolib.dummy;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.igrium.videolib.api.VideoHandle;
import com.igrium.videolib.api.VideoHandleFactory;
import com.igrium.videolib.api.VideoManager;
import com.igrium.videolib.api.VideoPlayer;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

/**
 * The video manager that gets loaded when no implementation is found.
 */
public class DummyVideoManager implements VideoManager {

    public static class DummyVideoHandle implements VideoHandle {

        @Override
        public Optional<String> getAddress() {
            return Optional.empty();
        }

        @Override
        public InputStream openStream() throws IOException {
            throw new IOException("Cannot open stream of dummy video handle.");
        }
    }

    public static class DummyVideoHandleFactory implements VideoHandleFactory {

        @Override
        public VideoHandle getVideoHandle(Identifier id) {
            return new DummyVideoHandle();
        }

        @Override
        public VideoHandle getVideoHandle(URL url) {
            return new VideoHandle.UrlVideoHandle(url);
        }
        
    }

    private final DummyVideoHandleFactory factory = new DummyVideoHandleFactory();
    private DummyVideoPlayer dummyPlayer = new DummyVideoPlayer(this);

    @Override
    public void close() throws Exception {
        
    }

    @Override
    public VideoPlayer getPlayer(Identifier id) {
        return dummyPlayer;
    }

    @Override
    public VideoPlayer getOrCreate(Identifier id) {
        return dummyPlayer;
    }

    @Override
    public boolean closePlayer(Identifier id) {
        return false;
    }

    @Override
    public IdentifiableResourceReloadListener getReloadListener() {
        return new SimpleSynchronousResourceReloadListener() {

            @Override
            public Identifier getFabricId() {
                return new Identifier("videolib", "dummyvideos");
            }

            @Override
            public void reload(ResourceManager var1) {                
            }
            
        };
    }

    @Override
    public Collection<String> supportedExtensions() {
        return Collections.emptySet();
    }

    @Override
    public VideoHandleFactory getVideoHandleFactory() {
        return factory;
    }
    
}
