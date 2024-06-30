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

import dev.greenadine.plcommons.annotation.Async;
import dev.greenadine.plcommons.annotation.Sync;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A utility class for Bukkit-related tasks.
 *
 * @since 0.1
 */
public final class BukkitUtils {

    private BukkitUtils() {
        throw new UnsupportedOperationException("Instantiation of utility class.");
    }

    /**
     * Formats the given message with color codes.
     *
     * @param message the message.
     * @return the formatted message.
     */
    public static @NotNull String formatColored(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Runs the given task synchronously. Useful for task that are required to be run on the main thread, such as
     * teleporting entities.
     * <p>
     * Only usable if the plugin is extended from {@link PLCommonsPlugin}.
     *
     * @param task The task to run.
     */
    @Sync
    public static void runSync(@NotNull Runnable task) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(PLCommons.getPluginInstance(), task);
        }
    }

    /**
     * Runs the given task asynchronously. Useful for task that are not required to be run on the main thread.
     * <p>
     * Only usable if the plugin is extended from {@link PLCommonsPlugin}.
     *
     * @param task the task to run.
     * @return the Bukkit task.
     */
    @Async
    public static @NotNull BukkitTask runAsync(@NotNull Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(PLCommons.getPluginInstance(), task);
    }

    /**
     * Runs the given task asynchronously after the given delay. Useful for task that are not required to be run on the main thread.
     * <p>
     * Only usable if the plugin is extended from {@link PLCommonsPlugin}.
     *
     * @param task             the task to run.
     * @param exceptionHandler the exception handler to run if an exception occurs.
     * @return the Bukkit task.
     */
    @Async
    public static @NotNull BukkitTask runAsync(@NotNull Runnable task, @NotNull Consumer<Throwable> exceptionHandler) {
        return Bukkit.getScheduler().runTaskAsynchronously(PLCommons.getPluginInstance(), () -> {
            try {
                task.run();
            } catch (Throwable ex) {
                exceptionHandler.accept(ex);
            }
        });
    }

    /**
     * Runs the given task synchronously after the given delay. Useful for task that are not required to be run on the main thread.
     *
     * @param task  the task to run.
     * @param delay the delay in ticks.
     * @return the Bukkit task.
     */
    public static @NotNull BukkitTask runLater(@NotNull Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(PLCommons.getPluginInstance(), task, delay);
    }

    /**
     * Runs the given task asynchronously after the given delay. Useful for task that are not required to be run on the main thread.
     *
     * @param task  the task to run.
     * @param delay the delay in ticks.
     * @return the Bukkit task.
     */
    @Async
    public static @NotNull BukkitTask runLaterAsync(@NotNull Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(PLCommons.getPluginInstance(), task, delay);
    }

    /**
     * Calls the given event. This method automatically calls the event on the main thread, and returns a callback that allows for quick
     * conditional follow-up actions to be performed after the event has been called.
     *
     * @param event the event to call.
     * @return a {@link EventCallback callback} for the event that allows for quick conditional follow-up actions to be performed after the
     * event has been called.
     */
    @Sync
    public static @NotNull EventCallback callEvent(@NotNull Event event) {
        runSync(() -> Bukkit.getPluginManager().callEvent(event));
        return new EventCallbackImpl(event);
    }
}
