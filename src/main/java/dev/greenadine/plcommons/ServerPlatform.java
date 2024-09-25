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

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.ExtendedBukkitCommandManager;
import co.aikar.commands.ExtendedPaperCommandManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * An enumeration of server platforms. This class is used to determine the server platform between Bukkit, Spigot, Paper, BungeeCord, Waterfall, and Velocity. The current server platform can
 * be retrieved using {@link ServerPlatform#getCurrent()}.
 *
 * @since 0.1
 */
public enum ServerPlatform {

    BUKKIT,
    SPIGOT,
    PAPER,
    BUNGEECORD,
    WATERFALL,
    VELOCITY,
    UNKNOWN
    ;

    private static final ServerPlatform CURRENT;
    static {
        CURRENT = detectCurrent();
        DebugLogger.info("Detected server platform: " + CURRENT);
    }

    /**
     * Gets the current server platform.
     *
     * @return the current server platform.
     */
    public static @NotNull ServerPlatform getCurrent() {
        return CURRENT;
    }

    /**
     * Detects the current server platform.
     *
     * @return the current server platform.
     */
    private static @NotNull ServerPlatform detectCurrent() {
        final String serverVersion = Bukkit.getServer().getVersion();
        if (serverVersion.contains("Bukkit"))
            return BUKKIT;
        else if (serverVersion.contains("Spigot"))
            return SPIGOT;
        else if (serverVersion.contains("Paper"))
            return PAPER;
        else if (serverVersion.contains("BungeeCord"))
            return BUNGEECORD;
        else if (serverVersion.contains("Waterfall"))
            return WATERFALL;
        else if (serverVersion.contains("Velocity"))
            return VELOCITY;
        else return UNKNOWN;
    }

    /* Internal */

    /**
     * Creates a new command manager for the current server platform.
     *
     * @return the new command manager.
     */
    static @NotNull BukkitCommandManager newCommandManager() {
        if (CURRENT == PAPER)
            return new ExtendedPaperCommandManager(PLCommons.getPlugin());
        return new ExtendedBukkitCommandManager(PLCommons.getPlugin());
    }
}
