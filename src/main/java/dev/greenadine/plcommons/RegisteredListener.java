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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class RegisteredListener {

    private final Listener listener;
    private final List<Class<? extends Event>> events = new ArrayList<>();

    RegisteredListener(@NotNull Listener listener) {
        this.listener = listener;
        final Class<?> listenerClass = listener.getClass();

        // Retrieve all classes of the events the listener has declared event handlers for
        for (Method method : listenerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class))
                continue;
            Class<?> paramType = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(paramType))
                continue;
            //noinspection unchecked
            events.add((Class<? extends Event>) paramType);
        }
        // If there are no declared event handlers, throw an exception
        if (events.isEmpty())
            throw new IllegalStateException("Listener " + listenerClass.getName() + " does not have any declared event handlers");

        register();  // Register the listener
    }

    /**
     * Registers the listener.
     */
    private void register() {
        Bukkit.getPluginManager().registerEvents(listener, PLCommons.getPlugin());
    }

    /**
     * Unregisters this listener from all events it is listening to.
     */
    void unregister() {
        for (Class<? extends Event> event : events) {
            final HandlerList handlerList = getHandlerList(event);
            handlerList.unregister(listener);
        }
    }

    private static final Cache<Class<? extends Event>, HandlerList> handlerListCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    /**
     * Gets the handler list for the given event class from cache. If the handler list is not cached, it will be retrieved using reflection from the 'getHandlerList()' method of the event
     * class and then cached.
     *
     * @param eventClass the event class.
     * @return the handler list.
     */
    private static @NotNull HandlerList getHandlerList(@NotNull Class<? extends Event> eventClass) {
        try {
            return handlerListCache.get(eventClass, () -> {
                try {
                    return (HandlerList) eventClass.getMethod("getHandlerList").invoke(null);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalArgumentException("Event class '" + eventClass.getName() + "' is missing its 'getHandlerList()' method. Please notify the developer of this event's plugin.", e);
                }
            });
        } catch (ExecutionException ex) {
            throw new IllegalStateException("Failed to get handler list for event class " + eventClass.getName() + ".", ex);
        }
    }
}
