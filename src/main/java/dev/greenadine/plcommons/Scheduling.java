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

import dev.greenadine.plcommons.annotation.Async;
import dev.greenadine.plcommons.annotation.Sync;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Utility class for providing a more functional way of using the Bukkit scheduler.
 *
 * @since 0.1
 */
public final class Scheduling {

    private Scheduling() {
    }

    private static final BukkitScheduler scheduler = Bukkit.getScheduler();
    private static final Plugin plugin = PLCommons.getPlugin();

    /**
     * Runs the given task synchronously. Useful for task that are required to be run on the main thread, such as
     * teleporting entities.
     * <p>
     * Only usable if the plugin is extended from {@link PLCommonsPlugin}.
     *
     * @param task The task to run.
     */
    @Sync
    public static BukkitTask runSync(@NotNull Runnable task) {
        return scheduler.runTask(plugin, task);
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
        return scheduler.runTaskAsynchronously(plugin, task);
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
        return scheduler.runTaskAsynchronously(plugin, () -> {
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
     * @param delay the delay in ticks.
     * @param task  the task to run.
     * @return the Bukkit task.
     */
    public static @NotNull BukkitTask runLater(long delay, @NotNull Runnable task) {
        return scheduler.runTaskLater(plugin, task, delay);
    }

    /**
     * Runs the given task asynchronously after the given delay. Useful for task that are not required to be run on the main thread.
     *
     * @param delay the delay in ticks.
     * @param task  the task to run.
     * @return the Bukkit task.
     */
    @Async
    public static @NotNull BukkitTask runLaterAsync(long delay, @NotNull Runnable task) {
        return scheduler.runTaskLaterAsynchronously(plugin, task, delay);
    }
}
