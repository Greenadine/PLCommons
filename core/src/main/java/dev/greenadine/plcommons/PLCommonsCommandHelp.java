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

import co.aikar.commands.MessageType;
import co.aikar.commands.RegisteredCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PLCommonsCommandHelp {

    private static final String[] HELP = {"help", "-help", "-h", "?", "-?"};
    private static final String CATCHUNKNOWN = "__catchunknown";

    private final PLCommonsLanguageManager languageManager;
    private final String commandName;
    private List<HelpEntry> helpEntries = new LinkedList<>();
    private int page = 1;
    private final int perPage = 10;  // TODO: Make configurable maybe?
    private int totalPages;

    PLCommonsCommandHelp(@NotNull String commandName, @NotNull Set<Map.Entry<String, RegisteredCommand>> subCommands, @NotNull PLCommonsLanguageManager languageManager) {
        this.languageManager = languageManager;
        this.commandName = commandName;

        // Iterate over subcommands
        final Set<RegisteredCommand> seen = new HashSet<>();
        for (Map.Entry<String, RegisteredCommand> entry : subCommands) {
            final String key = entry.getKey();
            if (key.equals(CATCHUNKNOWN) || Strings.equalsAny(key, HELP))
                continue;
            final RegisteredCommand command = entry.getValue();
            if (seen.contains(command))
                continue;
            helpEntries.add(new HelpEntry(command));
            seen.add(command);
        }
    }

    public void showHelp(CommandSender sender) {
        final String header = languageManager.formatMessage(sender, MessageType.INFO, PLCommonsMessageKeys.COMMAND_HELP_HEADER, "header", commandName, "page", String.valueOf(page), "totalPages", String.valueOf(totalPages));

        for (HelpEntry entry : helpEntries) {
            final String message = languageManager.formatMessage(sender, MessageType.INFO, PLCommonsMessageKeys.COMMAND_HELP_FORMAT, "command", commandName, "syntax", entry.command.getSyntaxText(), "description", entry.command.getHelpText());
        }
    }

    private static class HelpEntry {

        private final RegisteredCommand command;

        private HelpEntry(RegisteredCommand command) {
            this.command = command;
        }
    }
}
