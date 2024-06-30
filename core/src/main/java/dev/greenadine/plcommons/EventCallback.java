/*
 * Copyright 2024 Kevin Zuman
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.greenadine.plcommons;

import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface for event callbacks.
 *
 * @since 0.1
 */
public interface EventCallback {

    /**
     * Perform the following action after the event has been called by the plugin. This method does not guarantee execution on a specific
     * thread.
     *
     * @param runnable the action to perform.
     */
    @NotNull
    EventCallback andThen(@NotNull Runnable runnable);

    /**
     * Perform the following action after the event has been called by the plugin. This method ensures that the action is performed on the
     * main thread.
     *
     * @param runnable the action to perform.
     */
    @NotNull
    EventCallback andThenSync(@NotNull Runnable runnable);

    /**
     * Perform the following action asynchronously after the event has been called by the plugin.
     *
     * @param runnable the action to perform.
     */
    @NotNull
    EventCallback andThenAsync(@NotNull Runnable runnable);

    /**
     * Perform the following action after the event has been called by the plugin after a delay. This method does not guarantee execution
     * on a specific thread.
     *
     * @param runnable the action to perform.
     * @param delay    the delay before the action is performed, in ticks.
     * @return the event callback.
     */
    @NotNull
    EventCallback andThen(@NotNull Runnable runnable, long delay);

    /**
     * Perform the following action after the event has been called by the plugin after a delay. This method guarantees that the action is
     * performed on the main thread.
     *
     * @param runnable the action to perform.
     * @param delay    the delay before the action is performed, in ticks.
     * @return the event callback.
     */
    @NotNull
    EventCallback andThenSync(@NotNull Runnable runnable, long delay);

    /**
     * Perform the following action asynchronously after the event has been called by the plugin after a delay.
     *
     * @param runnable the action to perform.
     * @param delay    the delay before the action is performed, in ticks.
     * @return the event callback.
     */
    @NotNull
    EventCallback andThenAsync(@NotNull Runnable runnable, long delay);

    /**
     * Perform the following action if the event is cancelled. Only applies when the callback originates from a {@link Cancellable} event.
     * This method does not guarantee execution on a specific thread.
     *
     * @param runnable the action to perform.
     * @return the event callback.
     * @throws IllegalStateException if the called event is not cancellable.
     */
    @NotNull
    EventCallback onCancel(@NotNull Runnable runnable);

    /**
     * Perform the following action if the event was cancelled. Only applies when the callback originates from a {@link Cancellable} event.
     * This method ensures that the action is performed on the main thread.
     *
     * @param runnable the action to perform.
     * @return the event callback.
     * @throws IllegalStateException if the called event is not cancellable.
     */
    @NotNull
    EventCallback onCancelSync(@NotNull Runnable runnable);

    /**
     * Perform the following action asynchronously if the event was cancelled. Only applies when the callback originates from a
     * {@link Cancellable} event.
     *
     * @param runnable the action to perform.
     * @return the event callback.
     * @throws IllegalStateException if the called event is not cancellable.
     */
    @NotNull
    EventCallback onCancelAsync(@NotNull Runnable runnable);
}
