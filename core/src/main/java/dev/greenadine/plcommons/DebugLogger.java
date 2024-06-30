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

public class DebugLogger {

    private static boolean debug;
    private static final String PREFIX = "[Debug] ";

    /**
     * Sets whether debug messages should be printed to the console.
     *
     * @param enabled whether debug messages should be printed.
     */
    public static void setDebugEnabled(boolean enabled) {
        debug = enabled;
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
        if (debug) {
            PluginLogger.info(PREFIX + msg, thrown);
        }
    }

    /**
     * Logs a warning message to the console.
     *
     * @param msg the message to log.
     */
    public static void warning(String msg) {
        warning(msg, null);
    }

    /**
     * Logs a warning message and a {@link Throwable} to the console.
     *
     * @param msg    the message to log.
     * @param thrown the throwable to log.
     */
    public static void warning(String msg, Throwable thrown) {
        if (debug) {
            PluginLogger.warning(PREFIX + msg, thrown);
        }
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
        if (debug) {
            PluginLogger.severe(PREFIX + msg, thrown);
        }
    }
}
