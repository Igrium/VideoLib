package com.igrium.videolib.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.igrium.videolib.api.VideoHandle;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

/**
 * A video loader that copies videos into temp directory for easy streaming.
 * 
 * @param <T> The type of handle this loader will use.
 */
public class FileVideoLoader<T extends VideoHandle> implements IdentifiableResourceReloadListener {

    protected Consumer<Map<Identifier, T>> handleConsumer;
    protected Predicate<String> extensionFilter;
    protected Function<File, T> handleFactory;

    private static final Logger LOGGER = LogManager.getLogger("Video Loader");

    public FileVideoLoader(Predicate<String> extensionFilter, Function<File, T> handleFactory, Consumer<Map<Identifier, T>> handleConsumer) {
        this.extensionFilter = extensionFilter;
        this.handleFactory = handleFactory;
        this.handleConsumer = handleConsumer;
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler,
            Profiler applyProfiler,
            Executor prepareExecutor, Executor applyExecutor) {
        Map<Identifier, Resource> resources = manager.findResources("videos",
                filename -> extensionFilter.test(FilenameUtils.getExtension(filename.getPath())));
        
        Map<Identifier, T> files = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> futures = new LinkedList<>();

        for (var entry : resources.entrySet()) {
            futures.add(CompletableFuture.runAsync(() -> {
                LOGGER.info("Loading video {}", entry.getKey());
                try {
                    File file = loadVideo(entry.getKey(), entry.getValue());
                    files.put(entry.getKey(), handleFactory.apply(file));
                } catch (IOException e) {
                    LOGGER.error("Unable to load video: " + entry.getKey(), e);
                }
            }, prepareExecutor));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenCompose(synchronizer::whenPrepared).thenRunAsync(() -> {
                    handleConsumer.accept(files);
                }, applyExecutor);
    }

    protected File loadVideo(Identifier id, Resource resource) throws IOException {
        String ext = FilenameUtils.getExtension(id.getPath());
        File file = File.createTempFile("video_", "."+ext);

        InputStream inputStream = resource.getInputStream();
        OutputStream outputStream = new FileOutputStream(file);

        inputStream.transferTo(outputStream);

        inputStream.close();
        outputStream.close();

        return file;
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("videolib", "videoloader");
    }

}
