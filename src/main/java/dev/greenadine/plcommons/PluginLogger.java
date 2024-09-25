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
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * A utility class for logging messages.
 *
 * @since 0.1
 */
public final class PluginLogger {

    private PluginLogger() {
    }

    /**
     * Logs a message at {@link Level#INFO}.
     *
     * @param message the message to log.
     */
    public static void info(@Nullable String message) {
        info(message, null);
    }

    /**
     * Logs a message and a {@link Throwable} at {@link Level#INFO}.
     *
     * @param message the message to log.
     * @param thrown  the throwable to log.
     */
    public static void info(@Nullable String message, @Nullable Throwable thrown) {
        log(Level.INFO, message, thrown);
    }

    /**
     * Logs a message at {@link Level#WARNING}.
     *
     * @param message the message to log.
     */
    public static void warn(@Nullable String message) {
        warn(message, null);
    }

    /**
     * Logs a message and a {@link Throwable} at {@link Level#WARNING}.
     *
     * @param message the message to log.
     * @param thrown  the throwable to log.
     */
    public static void warn(@Nullable String message, @Nullable Throwable thrown) {
        log(Level.WARNING, message, thrown);
    }

    /**
     * Logs a message at {@link Level#SEVERE}.
     *
     * @param message the message to log.
     */
    public static void severe(@Nullable String message) {
        severe(message, null);
    }

    /**
     * Logs a message and a {@link Throwable} at {@link Level#SEVERE}.
     *
     * @param message the message to log.
     * @param thrown  the throwable to log.
     */
    public static void severe(@Nullable String message, @Nullable Throwable thrown) {
        log(Level.SEVERE, message, thrown);
    }

    /**
     * Logs a message and a throwable at a specific log level.
     *
     * @param level   the log level.
     * @param message the message to log.
     * @param thrown  the throwable to log.
     */
    private static void log(@NotNull Level level, @Nullable String message, @Nullable Throwable thrown) {
        PLCommons.getPlugin().getLogger().log(level, message, thrown);
    }
}
