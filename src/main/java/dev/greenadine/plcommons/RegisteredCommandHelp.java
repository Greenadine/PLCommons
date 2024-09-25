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

import co.aikar.commands.RegisteredCommand;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class RegisteredCommandHelp {

    private static final String[] ignoredSubcommands = {"help", "-help", "-h", "?", "-?", "__catchunknown", "__default"};

    final String commandName;
    final TreeSet<HelpEntry> helpEntries = new TreeSet<>();

    final PLCLanguageManager languageManager;
    int entriesPerPage = 5;

    RegisteredCommandHelp(String commandName, Set<Map.Entry<String, RegisteredCommand>> subCommands, PLCLanguageManager languageManager) {
        this.languageManager = languageManager;
        this.commandName = commandName;

        // Iterate over subcommands
        final Set<String> seen = new HashSet<>();
        for (Map.Entry<String, RegisteredCommand> entry : subCommands) {
            final String key = entry.getKey();
            if (Strings.equalsAny(key, ignoredSubcommands))
                continue;
            final String subCommand = entry.getKey();
            final RegisteredCommand command = entry.getValue();
            if (seen.contains(subCommand))
                continue;
            helpEntries.add(new HelpEntry(subCommand, command));
            seen.add(subCommand);
        }
    }

    static class HelpEntry implements Comparable<HelpEntry> {

        final String name;
        final String syntax;
        final String description;
        final Set<String> requiredPermissions;

        private HelpEntry(String subCommand, RegisteredCommand<?> registeredCommand) {
            this.name = subCommand;
            this.syntax = registeredCommand.getSyntaxText();
            this.description = registeredCommand.getHelpText();
            this.requiredPermissions = registeredCommand.getRequiredPermissions();
        }

        @Override
        public int compareTo(@NotNull RegisteredCommandHelp.HelpEntry o) {
            return name.compareTo(o.name);
        }
    }
}
