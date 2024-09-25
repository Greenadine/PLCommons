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

import co.aikar.commands.ACFUtil;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * PLCommons version of {@link co.aikar.commands.MessageFormatter}.
 *
 * @since 0.1
 */
public class PLCMessageFormatter {

    // TODO: Add support for Paper NamedTextColor

    private final List<ChatColor> colors = new ArrayList<>();

    public PLCMessageFormatter(ChatColor... colors) {
        this.colors.addAll(Arrays.asList(colors));
    }

    public String format(int index, String message) {
        try {
            return colors.get(index) + message;
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Invalid color index: " + index);
        }
    }

    public ChatColor getColor(int index) {
        if (index > 0)
            --index;
        else index = 0;

        ChatColor color = this.colors.get(index);
        if (color == null)
            color = this.getDefaultColor();
        return color;
    }

    public ChatColor getDefaultColor() {
        return this.getColor(1);
    }

    public String format(String message) {
        if (message == null || message.isEmpty())
            return "";

        final String def = format(0, "");
        final Matcher matcher = PLCPatterns.COLOR_FORMATTER.matcher(message);
        final StringBuffer sb = new StringBuffer(message.length());
        sb.append(def);

        while (matcher.find()) {
            final String code = matcher.group("code");
            final char[] chars = code.toCharArray();
            final int color = ACFUtil.parseInt(String.valueOf(chars[0]), 1);
            final char styleCodeChar = chars.length >= 2 ? chars[1] : 'r';
            final String msg = getChatStyle(styleCodeChar) + format(color, matcher.group("msg")) + def;
            matcher.appendReplacement(sb, Matcher.quoteReplacement(msg));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String formatAlt(String message) {
        if (message == null || message.isEmpty())
            return "";
        final char[] chars = message.toCharArray();

        for (int i = 0; i < chars.length - 1; ++i) {
            if (chars[i] == '&' && "123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) {
                chars[i] = ChatColor.COLOR_CHAR;
                final char code = Character.toLowerCase(chars[i + 1]);
                if (Character.isDigit(chars[i + 1]))
                    chars[i + 1] = colors.get(ACFUtil.parseInt(String.valueOf(code))).getChar();
                else
                    chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }

    private static @NotNull ChatColor getChatStyle(char code) {
        switch (code) {
            case 'k':
                return ChatColor.MAGIC;
            case 'l':
                return ChatColor.BOLD;
            case 'm':
                return ChatColor.STRIKETHROUGH;
            case 'n':
                return ChatColor.UNDERLINE;
            case 'o':
                return ChatColor.ITALIC;
        }
        return ChatColor.RESET;
    }
}
