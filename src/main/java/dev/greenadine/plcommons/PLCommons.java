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

import org.jetbrains.annotations.NotNull;

/**
 * A class that provides access to the plugin instance and the {@link DependencyManager}.
 *
 * @since 0.1
 */
public final class PLCommons {

    private static final DependencyManager dependencyManager = new DependencyManager();
    static PLCommonsPlugin pluginInstance;

    /**
     * Returns the plugin instance.
     *
     * @return the plugin instance
     * @throws IllegalStateException if this method is called before the plugin instance has been set internally.
     */
    public static @NotNull PLCommonsPlugin getPlugin() {
        if (pluginInstance == null)
            throw new IllegalStateException("Plugin instance has not been set; are you calling this method too early?");
        return pluginInstance;
    }

    /**
     * Gets the dependency manager.
     *
     * @return the dependency manager.
     */
    public static @NotNull DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    /**
     * Handles an exception that occurs. This can be overridden in the {@link PLCommonsPlugin} instance to provide custom handling.
     *
     * @param thrown the exception that was thrown.
     */
    public static void handleThrown(@NotNull Throwable thrown) {
        getPlugin().handleThrown(thrown);
    }

    /**
     * Runs a {@link Runnable} and handles any exceptions that occur through the plugin's exception handling.
     *
     * @param runnable the runnable to run.
     */
    public static void handleThrown(@NotNull Runnable runnable) {
        final Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler((thread1, thrown) -> handleThrown(thrown));
        thread.start();
    }
}
