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

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * An enumeration of Bukkit versions. This class is used to determine the version of the server between 1.8 and 1.21. The current version of the server can be retrieved using
 * {@link BukkitVersion#getVersion()}. Versions older than 1.8 are labeled as {@link BukkitVersion#LEGACY} and versions newer than 1.21 are labeled as {@link BukkitVersion#UNKNOWN}.
 *
 * @since 0.1
 */
public enum BukkitVersion {

    /**
     * An unknown or unsupported Bukkit version.
     */
    UNKNOWN,
    /**
     * A legacy Bukkit version (prior to 1.8).
     */
    LEGACY,
    V1_8,
    V1_9,
    V1_10,
    V1_11,
    V1_12,
    V1_13,
    V1_14,
    V1_15,
    V1_16,
    V1_17,
    V1_18,
    V1_19,
    V1_20,
    V1_21
    ;

    private static final BukkitVersion current;
    static {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        BukkitVersion temp;
        try {
            temp = valueOf(version);
        } catch (IllegalArgumentException ex) {
            temp = UNKNOWN;
        }
        current = temp;

        // Log unknown or legacy Bukkit versions
        if (current == UNKNOWN)
            DebugLogger.warn("Unknown Bukkit version: " + version);
        else if (current == LEGACY)
            DebugLogger.warn("Legacy Bukkit version: " + version);
    }

    /**
     * Gets the Bukkit version of the server.
     *
     * @return the Bukkit version.
     */
    public static @NotNull BukkitVersion getVersion() {
        return current;
    }

    /**
     * Checks if the current Bukkit version is the provided version or newer.
     *
     * @param version the version to check against.
     * @return {@code true} if the current Bukkit version is the provided version or newer, {@code false} otherwise.
     */
    public static boolean isAtLeast(@NotNull BukkitVersion version) {
        return current.ordinal() >= version.ordinal();
    }

    /**
     * Checks if the current Bukkit version is the provided version or older.
     *
     * @param version the version to check against.
     * @return {@code true} if the current Bukkit version is the provided version or older, {@code false} otherwise.
     */
    public static boolean isAtMost(@NotNull BukkitVersion version) {
        return current.ordinal() <= version.ordinal();
    }

    /**
     * Checks if the current Bukkit version is between the provided versions.
     *
     * @param min the minimum version (inclusive).
     * @param max the maximum version (inclusive).
     * @return {@code true} if the current Bukkit version is between the provided versions, {@code false} otherwise.
     */
    public static boolean isBetween(@NotNull BukkitVersion min, @NotNull BukkitVersion max) {
        return current.ordinal() >= min.ordinal() && current.ordinal() <= max.ordinal();
    }
}
