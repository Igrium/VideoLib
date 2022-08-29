package com.igrium.videolib.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * A simple event emitter with a variable listener type.
 * 
 * @param <T> The event "context" type.
 */
public class EventEmitter<T> {
    private Executor executor;
    protected final Set<Consumer<T>> listeners = new HashSet<>();

    public EventEmitter() {}
    public EventEmitter(Executor executor) {
        this.executor = executor;
    }

    public Executor getExecutor() {
        return executor;
    }

    /**
     * Register an event listener.
     * @param listener The listener.
     */
    public synchronized void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    /**
     * Unregister an event listener;
     * @param listener The listener.
     * @return 
     */
    public synchronized boolean removeListener(Consumer<?> listener) {
        return listeners.remove(listener);
    }
    
    public void invoke(T context) {
        if (executor != null) {
            executor.execute(() -> invokeImpl(context));
        } else {
            invokeImpl(context);
        }
    }

    protected void invokeImpl(T context) {
        // Copy the set so that listeners may remove themselves during execution
        Set<Consumer<T>> copy;
        synchronized(this) {
            copy = Set.copyOf(listeners);
        }
        for (Consumer<T> listener : copy) {
            listener.accept(context);
        }
    }
}
