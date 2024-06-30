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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * A utility class for logging messages.
 *
 * @since 0.1
 */
public final class PluginLogger {

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
    public static void warning(@Nullable String message) {
        warning(message, null);
    }

    /**
     * Logs a message and a {@link Throwable} at {@link Level#WARNING}.
     *
     * @param message the message to log.
     * @param thrown  the throwable to log.
     */
    public static void warning(@Nullable String message, @Nullable Throwable thrown) {
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
        PLCommons.getPluginInstance().getLogger().log(level, message, thrown);
    }
}
