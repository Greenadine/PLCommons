/*
 * Copyright (C) 2024 Greenadine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.greenadine.plcommons;

import com.google.common.base.Preconditions;
import dev.greenadine.plcommons.annotation.Sync;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static dev.greenadine.plcommons.Scheduling.runSync;

/**
 * A utility class for event-related tasks.
 *
 * @since 0.1
 */
public final class Events {

    private Events() {
    }

    private static final Map<Class<? extends Listener>, RegisteredListener> listeners = new HashMap<>();

    /**
     * Registers a listener.
     *
     * @param listener the listener.
     */
    public static void registerListener(@NotNull Listener listener) {
        final RegisteredListener plcListener = new RegisteredListener(listener);
        listeners.put(listener.getClass(), plcListener);
    }

    /**
     * Registers multiple listeners.
     *
     * @param listeners the listeners.
     */
    public static void registerListeners(@NotNull Listener... listeners) {
        for (Listener listener : listeners)
            registerListener(listener);
    }

    /**
     * Unregisters a listener.
     *
     * @param listenerClass the listener class.
     */
    public static void unregisterListener(@NotNull Class<? extends Listener> listenerClass) {
        Preconditions.checkState(listeners.containsKey(listenerClass), "Listener " + listenerClass.getName() + " is not registered");
        listeners.get(listenerClass).unregister();
    }

    /**
     * Unregisters multiple listeners.
     *
     * @param listenerClasses the listener classes.
     */
    @SafeVarargs
    public static void unregisterListeners(@NotNull Class<? extends Listener>... listenerClasses) {
        for (Class<? extends Listener> listenerClass : listenerClasses)
            unregisterListener(listenerClass);
    }

    /**
     * Unregisters all listeners.
     */
    public static void unregisterListeners() {
        listeners.values().forEach(RegisteredListener::unregister);
    }

    /**
     * Calls a given event. This method automatically calls the event on the main thread.
     *
     * @param event the event to call.
     */
    @Sync
    public static void call(@NotNull Event event) {
        runSync(() -> Bukkit.getPluginManager().callEvent(event));
    }

    /**
     * Calls a given event. This method automatically calls the event on the main thread, and returns a callback that allows for quick
     * (conditional) follow-up actions to be performed after the event has been called.
     *
     * @param event the event to call.
     * @return a {@link EventCallback callback} for the event that allows for quick conditional follow-up actions to be performed after the
     * event has been called.
     */
    @Sync
    public static @NotNull EventCallback callAnd(@NotNull Event event) {
        call(event);
        return new EventCallback(event);
    }
}
