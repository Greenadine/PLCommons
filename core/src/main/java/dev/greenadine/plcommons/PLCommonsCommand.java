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

import co.aikar.commands.BaseCommand;
import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Super class for {@link BaseCommand}s using the {@link PLCommonsCommandManager}.
 *
 * @since 0.1
 */
public abstract class PLCommonsCommand extends BaseCommand {

    private PLCommonsLanguageManager languageManager;
    private String prefix;

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param key the key of the message to send.
     */
    protected void sendMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key) {
        languageManager.sendMessage(sender, key);
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param sender       the sender.
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    protected void sendMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        languageManager.sendMessage(sender, key, replacements);
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param sender  the sender.
     * @param message the message to send.
     */
    protected void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        languageManager.sendMessage(sender, message);
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param sender       the sender.
     * @param message      the message to send.
     * @param replacements the replacements to apply to the message.
     */
    protected void sendMessage(@NotNull CommandSender sender, @NotNull String message, Object... replacements) {
        languageManager.sendMessage(sender, message, replacements);
    }

    /**
     * Sends a message to the sender.
     *
     * @param key the key of the message to send.
     */
    protected void sendRawMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key) {
        languageManager.sendRawMessage(sender, key);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender       the sender.
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    protected void sendRawMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        languageManager.sendRawMessage(sender, key, replacements);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender  the sender.
     * @param message the message to send.
     */
    protected void sendRawMessage(@NotNull CommandSender sender, @NotNull String message) {
        languageManager.sendRawMessage(sender, message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender       the sender.
     * @param message      the message to send.
     * @param replacements the replacements to apply to the message.
     */
    protected void sendRawMessage(@NotNull CommandSender sender, @NotNull String message, Object... replacements) {
        languageManager.sendRawMessage(sender, message, replacements);
    }

    /**
     * Gets the command prefix from its message key.
     *
     * @param sender the command sender.
     * @return the command prefix.
     */
    private @NotNull String getPrefix(@NotNull CommandSender sender) {
        if (prefix == null)
            prefix = languageManager.getMessage(sender, MessageKey.of("pl-commons.plugin_prefix"));
        return prefix;
    }

    /**
     * Called when the command is registered.
     *
     * @param languageManager the PLCommons language manager.
     */
    void onRegister(@NotNull PLCommonsLanguageManager languageManager) {
        this.languageManager = languageManager;
    }
}
