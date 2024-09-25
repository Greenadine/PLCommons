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
import co.aikar.locales.MessageKeyProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Represents either a player or console command issuer.
 *
 * @since 0.1
 */
public class PLCCommandIssuer {

    private final CommandSender sender;
    private final PLCLanguageManager languageManager;

    PLCCommandIssuer(CommandSender sender, PLCLanguageManager languageManager) {
        this.sender = sender;
        this.languageManager = languageManager;
    }

    /**
     * Sends an info message to the issuer.
     *
     * @param key the key of the message to send.
     */
    public void sendInfo(@NotNull MessageKeyProvider key) {
        sendMessage(MessageType.INFO, key);
    }

    /**
     * Sends an info message to the issuer.
     *
     * @param key    the key of the message to send.
     * @param prefix {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     */
    public void sendInfo(@NotNull MessageKeyProvider key, boolean prefix) {
        sendMessage(MessageType.INFO, key, prefix);
    }

    /**
     * Sends an info message to the issuer.
     *
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendInfo(@NotNull MessageKeyProvider key, String... replacements) {
        sendMessage(MessageType.INFO, key, replacements);
    }

    /**
     * Sends an info message to the issuer.
     *
     * @param key          the key of the message to send.
     * @param prefix       {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     * @param replacements the replacements to apply to the message.
     */
    public void sendInfo(@NotNull MessageKeyProvider key, boolean prefix, String... replacements) {
        sendMessage(MessageType.INFO, key, prefix, replacements);
    }

    /**
     * Sends an info message to the issuer.
     *
     * @param message the message to send.
     */
    public void sendInfo(@NotNull String message) {
        sendMessage(message);
    }

    /**
     * Sends a syntax message to the issuer.
     *
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendSyntax(@NotNull MessageKeyProvider key, String... replacements) {
        sendMessage(MessageType.SYNTAX, key, replacements);
    }

    /**
     * Sends an error message to the issuer.
     *
     * @param key the key of the message to send.
     */
    public void sendError(@NotNull MessageKeyProvider key) {
        sendMessage(MessageType.ERROR, key);
    }

    /**
     * Sends an error message to the issuer.
     *
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendError(@NotNull MessageKeyProvider key, String... replacements) {
        sendMessage(MessageType.ERROR, key, replacements);
    }

    /**
     * Sends an error message to the issuer.
     *
     * @param key    the key of the message to send.
     * @param prefix {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     */
    public void sendError(@NotNull MessageKeyProvider key, boolean prefix) {
        sendMessage(MessageType.ERROR, key, prefix);
    }

    /**
     * Sends an error message to the issuer.
     *
     * @param key          the key of the message to send.
     * @param prefix       {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     * @param replacements the replacements to apply to the message.
     */
    public void sendError(@NotNull MessageKeyProvider key, boolean prefix, String... replacements) {
        sendMessage(MessageType.ERROR, key, prefix, replacements);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param type the type of the message.
     * @param key  the key of the message to send.
     */
    public void sendMessage(@NotNull MessageType type, @NotNull MessageKeyProvider key) {
        sendMessage(type, key, true);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param type   the type of the message.
     * @param key    the key of the message to send.
     * @param prefix {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     */
    public void sendMessage(@NotNull MessageType type, @NotNull MessageKeyProvider key, boolean prefix) {
        languageManager.sendMessage(sender, type, key, prefix);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param type         the type of the message.
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull MessageType type, @NotNull MessageKeyProvider key, String... replacements) {
        sendMessage(type, key, true, replacements);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param type         the type of the message.
     * @param key          the key of the message to send.
     * @param prefix       {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull MessageType type, @NotNull MessageKeyProvider key, boolean prefix, String... replacements) {
        languageManager.sendMessage(sender, type, key, prefix, replacements);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param message the message to send.
     */
    public void sendMessage(@NotNull String message) {
        sendMessage(message, true);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param message the message to send.
     * @param prefix  {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     */
    public void sendMessage(@NotNull String message, boolean prefix) {
        languageManager.sendMessage(sender, message, prefix);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param message      the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull String message, String... replacements) {
        sendMessage(message, true, replacements);
    }

    /**
     * Sends a message to the issuer.
     *
     * @param message      the message to send.
     * @param prefix       {@code true} if the message should be prefixed with the plugin prefix, {@code false} otherwise.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull String message, boolean prefix, String... replacements) {
        languageManager.sendMessage(sender, message, prefix, replacements);
    }
}
