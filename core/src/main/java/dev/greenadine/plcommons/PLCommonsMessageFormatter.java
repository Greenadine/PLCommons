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

import co.aikar.commands.ACFUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * PLCommons version of {@link co.aikar.commands.MessageFormatter}.
 */
public class PLCommonsMessageFormatter {

    private final List<ChatColor> colors = new ArrayList<>();

    public PLCommonsMessageFormatter(ChatColor... colors) {
        this.colors.addAll(Arrays.asList(colors));
    }

    public String format(int index, String message) {
        return colors.get(index) + message;
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
        if (message == null)
            return "";

        final String def = format(1, "");
        final Matcher matcher = PLCommonsPatterns.COLORS.matcher(message);
        final StringBuffer sb = new StringBuffer(message.length());

        while (matcher.find()) {
            final int color = ACFUtil.parseInt(matcher.group("code"), 1);
            final String msg = this.format(color, matcher.group("msg")) + def;
            matcher.appendReplacement(sb, Matcher.quoteReplacement(msg));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
