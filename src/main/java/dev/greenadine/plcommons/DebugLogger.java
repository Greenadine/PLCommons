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

/**
 * Plugin logger for debugging purposes.
 *
 * @since 0.1
 */
public final class DebugLogger {

    private DebugLogger() {
    }

    private static boolean enabled;
    private static final String PREFIX = "[Debug] ";

    /**
     * Sets whether debug messages should be printed to the console.
     *
     * @param enabled whether debug messages should be printed.
     */
    public static void setEnabled(boolean enabled) {
        DebugLogger.enabled = enabled;
    }

    /**
     * Logs an info message to the console.
     *
     * @param msg the message to log.
     */
    public static void info(String msg) {
        info(msg, null);
    }

    /**
     * Logs an info message and a {@link Throwable} to the console.
     *
     * @param msg    the message to log.
     * @param thrown the throwable to log.
     */
    public static void info(String msg, Throwable thrown) {
        if (enabled)
            PluginLogger.info(PREFIX + msg, thrown);
    }

    /**
     * Logs a warning message to the console.
     *
     * @param msg the message to log.
     */
    public static void warn(String msg) {
        warn(msg, null);
    }

    /**
     * Logs a warning message and a {@link Throwable} to the console.
     *
     * @param msg    the message to log.
     * @param thrown the throwable to log.
     */
    public static void warn(String msg, Throwable thrown) {
        if (enabled)
            PluginLogger.warn(PREFIX + msg, thrown);
    }

    /**
     * Logs a severe message to the console.
     *
     * @param msg the message to log.
     */
    public static void severe(String msg) {
        severe(msg, null);
    }

    /**
     * Logs a severe message and a {@link Throwable} to the console.
     *
     * @param msg    the message to log.
     * @param thrown the throwable to log.
     */
    public static void severe(String msg, Throwable thrown) {
        if (enabled)
            PluginLogger.severe(PREFIX + msg, thrown);
    }
}
