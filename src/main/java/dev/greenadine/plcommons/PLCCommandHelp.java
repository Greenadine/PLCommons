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

import co.aikar.commands.MessageType;
import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A command help object that is used to display help messages for a command.
 *
 * @since 0.1
 */
public class PLCCommandHelp {

    private final RegisteredCommandHelp commandHelp;
    private final CommandSender sender;
    private final PLCLanguageManager languageManager;
    private int entriesPerPage = 5;

    PLCCommandHelp(RegisteredCommandHelp commandHelp, CommandSender sender, PLCLanguageManager languageManager) {
        this.commandHelp = commandHelp;
        this.sender = sender;
        this.languageManager = languageManager;
    }

    /**
     * Sets the amount of entries to display per page.
     *
     * @param entriesPerPage the new amount of entries per page.
     */
    public @NotNull PLCCommandHelp setEntriesPerPage(int entriesPerPage) {
        Preconditions.checkArgument(entriesPerPage > 0, "Entries per page must be greater than 0");
        this.entriesPerPage = entriesPerPage;
        return this;
    }

    /**
     * Shows the first help page.
     */
    public void show() {
        show(1);
    }

    /**
     * Shows the given help page.
     *
     * @param page the page.
     */
    public void show(int page) {
        final int totalPages = getPages(new ArrayList<>(commandHelp.helpEntries), entriesPerPage);
        final String pluginName = languageManager.getMessage(sender, PLCMessageKeys.PLUGIN_NAME);
        final String pages = totalPages == 1 ? "" : "(" + page + "/" + totalPages + ")";
        final String header = languageManager.formatMessage(sender, MessageType.HELP, PLCMessageKeys.COMMAND_HELP_HEADER,
                "plugin_name", pluginName, "pages", pages);

        sender.sendMessage("");
        sender.sendMessage(header);

        final ArrayList<RegisteredCommandHelp.HelpEntry> entries = getPage(new ArrayList<>(commandHelp.helpEntries), page, entriesPerPage);
        for (RegisteredCommandHelp.HelpEntry entry : entries) {
            if (!entry.requiredPermissions.isEmpty()) {
                boolean hasPermission = false;
                for (String permission : entry.requiredPermissions) {
                    if (sender.hasPermission(permission)) {
                        hasPermission = true;
                        break;
                    }
                }
                if (!hasPermission)
                    continue;
            }
            final String syntax = !entry.syntax.isEmpty() ? " " + entry.syntax : "";
            final String message = languageManager.formatMessage(sender, MessageType.HELP, PLCMessageKeys.COMMAND_HELP_ENTRY,
                    "command", commandHelp.commandName, "sub_command", entry.name, "syntax", syntax, "description", entry.description);
            sender.sendMessage(message);
        }
    }

    /**
     * Returns the amount of pages.
     *
     * @param list            the ArrayList
     * @param elementsPerPage the amount of elements to display per page.
     * @return the amount of pages.
     */
    static <T> int getPages(ArrayList<T> list, float elementsPerPage) {
        return (int) Math.ceil(list.size() / elementsPerPage);
    }

    /**
     * Returns a new ArrayList containing the entries within the given indexes.
     *
     * @param list            the original ArrayList.
     * @param currentPage     the page.
     * @param elementsPerPage the amount of elements to display per page.
     * @return a new ArrayList containing the entries within the given indexes,
     */
    static <T> @NotNull ArrayList<T> getPage(ArrayList<T> list, int currentPage, int elementsPerPage) {
        final ArrayList<T> result = new ArrayList<>();
        int from = ((currentPage - 1) * elementsPerPage);
        for (int i = from; i < from + elementsPerPage; i++) {
            try {
                result.add(list.get(i));
            } catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return result;
    }
}
