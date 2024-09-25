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

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for certain string operations, including colorizing strings with {@link ChatColor} codes.
 *
 * @since 0.1
 */
public final class Strings {

    private Strings() {
    }

    /**
     * Checks if the given string equals any of the given strings.
     *
     * @param string  the string to check.
     * @param strings the strings to compare.
     * @return true if the string equals any of the given strings, false otherwise.
     */
    public static boolean equalsAny(String string, String... strings) {
        for (final String str : strings)
            if (string.equals(str))
                return true;
        return false;
    }

    /**
     * Formats the given message with alternate {@link ChatColor} codes using the '&' character.
     *
     * @param message the message.
     * @return the formatted message.
     */
    public static @NotNull String colorize(@NotNull String message) {
        return colorize('&', message);
    }

    /**
     * Formats the given message with alternate {@link ChatColor} codes using the given alternate color character.
     *
     * @param altColorChar the alternate color character.
     * @param message      the message.
     * @return the formatted message.
     */
    public static @NotNull String colorize(char altColorChar, @NotNull String message) {
        return ChatColor.translateAlternateColorCodes(altColorChar, message);
    }
}
