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

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import static dev.greenadine.plcommons.Scheduling.*;

/**
 * Provides a fluent API for easily performing (conditional) actions after calling an {@link Event}.
 *
 * @see Events#callAnd(Event)
 * @since 0.1
 */
public class EventCallback {

    private final boolean cancellable;
    private final boolean cancelled;

    EventCallback(@NotNull Event event) {
        this.cancellable = event instanceof Cancellable;
        this.cancelled = cancellable && ((Cancellable) event).isCancelled();
    }

    /**
     * Perform the following action after the event has been called by the plugin. This method does not guarantee execution on a specific
     * thread.
     *
     * @param runnable the action to perform.
     */
    public @NotNull EventCallback then(@NotNull Runnable runnable) {
        if (!cancelled)
            runnable.run();
        return this;
    }

    /**
     * Perform the following action after the event has been called by the plugin. This method ensures that the action is performed on the
     * main thread.
     *
     * @param runnable the action to perform.
     */
    public @NotNull EventCallback thenSync(@NotNull Runnable runnable) {
        if (!cancelled)
            runSync(runnable);
        return this;
    }

    /**
     * Perform the following action asynchronously after the event has been called by the plugin.
     *
     * @param runnable the action to perform.
     */
    public @NotNull EventCallback thenAsync(@NotNull Runnable runnable) {
        if (!cancelled)
            runAsync(runnable);
        return this;
    }

    /**
     * Perform the following action after the event has been called by the plugin after a delay. This method guarantees that the action is
     * performed on the main thread.
     *
     * @param runnable the action to perform.
     * @param delay    the delay before the action is performed, in ticks.
     * @return the event callback.
     */
    public @NotNull EventCallback thenSync(@NotNull Runnable runnable, long delay) {
        if (!cancelled)
            runLater(delay, runnable);
        return this;
    }

    /**
     * Perform the following action asynchronously after the event has been called by the plugin after a delay.
     *
     * @param runnable the action to perform.
     * @param delay    the delay before the action is performed, in ticks.
     * @return the event callback.
     */
    public @NotNull EventCallback thenAsync(@NotNull Runnable runnable, long delay) {
        if (!cancelled)
            runLaterAsync(delay, runnable);
        return this;
    }

    /**
     * Perform the following action if the event is cancelled. Only applies when the callback originates from a {@link Cancellable} event.
     * This method does not guarantee execution on a specific thread.
     *
     * @param runnable the action to perform.
     * @return the event callback.
     * @throws IllegalStateException if the called event is not cancellable.
     */
    public @NotNull EventCallback onCancel(@NotNull Runnable runnable) {
        if (!cancellable)
            throw new IllegalStateException("Event is not cancellable");
        if (cancelled)
            runnable.run();
        return this;
    }

    /**
     * Perform the following action if the event was cancelled. Only applies when the callback originates from a {@link Cancellable} event.
     * This method ensures that the action is performed on the main thread.
     *
     * @param runnable the action to perform.
     * @return the event callback.
     * @throws IllegalStateException if the called event is not cancellable.
     */
    public @NotNull EventCallback onCancelSync(@NotNull Runnable runnable) {
        if (!cancellable)
            throw new IllegalStateException("Event is not cancellable");
        if (cancelled)
            runSync(runnable);
        return this;
    }

    /**
     * Perform the following action asynchronously if the event was cancelled. Only applies when the callback originates from a
     * {@link Cancellable} event.
     *
     * @param runnable the action to perform.
     * @return the event callback.
     * @throws IllegalStateException if the called event is not cancellable.
     */
    public @NotNull EventCallback onCancelAsync(@NotNull Runnable runnable) {
        if (!cancellable)
            throw new IllegalStateException("Event is not cancellable");
        if (cancelled)
            runAsync(runnable);
        return this;
    }
}
