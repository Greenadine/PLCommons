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

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.CommandManager;
import co.aikar.commands.MessageType;
import co.aikar.locales.LocaleManager;
import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PLCommonsLanguageManager {

    private static PLCommonsLanguageManager instance;

    /**
     * Gets the current instance of the language manager.
     *
     * @return the current instance of the language manager.
     * @throws IllegalStateException if the language manager is not initialized.
     */
    public static PLCommonsLanguageManager getCurrentInstance() {
        if (instance == null)
            throw new IllegalStateException("PLCommonsLanguageManager is not initialized");
        return instance;
    }

    private final LocaleManager<CommandSender> localeManager;
    private final Set<Locale> supportedLanguages = new HashSet<>();
    private final Map<MessageType, PLCommonsMessageFormatter> messageFormatters = new IdentityHashMap<>();
    private PLCommonsMessageFormatter defaultFormatter;
    private String pluginPrefix;

    public PLCommonsLanguageManager(@NotNull Function<CommandSender, Locale> localeMapper, @Nullable Locale defaultLocale, Locale... supportedLanguages) {
        if (instance != null)
            throw new IllegalStateException("PLCommonsLanguageManager is already initialized");
        instance = this;
        this.localeManager = LocaleManager.create(localeMapper, defaultLocale);
        this.supportedLanguages.add(defaultLocale);
        this.supportedLanguages.addAll(Arrays.asList(supportedLanguages));
    }

    /**
     * Gets all supported languages.
     *
     * @return the supported languages.
     */
    @NotNull
    public Set<Locale> getSupportedLanguages() {
        return supportedLanguages;
    }

    /**
     * Checks if the given locale is a supported language.
     *
     * @param locale the locale to check.
     * @return true if the locale is a supported language, {@code false} otherwise.
     */
    public boolean isSupportedLanguage(@NotNull Locale locale) {
        return supportedLanguages.contains(locale);
    }

    /**
     * Adds a message bundle to the language manager.
     *
     * @param bundleName the name of the message bundle.
     */
    public boolean addMessageBundle(@NotNull String bundleName) {
        return localeManager.addMessageBundle(bundleName, supportedLanguages.toArray(new Locale[0]));
    }

    /**
     * Sets the default message format.
     *
     * @param colors the colors to use in the default message format.
     */
    public void setDefaultFormat(ChatColor... colors) {
        defaultFormatter = new PLCommonsMessageFormatter(colors);
    }

    /**
     * Sets the message format for the given type.
     *
     * @param type the message type.
     * @param colors the colors to use in the message format.
     */
    public void setFormat(@NotNull MessageType type, @NotNull ChatColor... colors) {
        messageFormatters.put(type, new PLCommonsMessageFormatter(colors));
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param key the key of the message to send.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key) {
        final String message = formatMessage(sender, MessageType.INFO, key);
        sender.sendMessage(getPrefix(sender) + message);
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param sender       the sender.
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        final String message = formatMessage(sender, MessageType.INFO, key, replacements);
        sender.sendMessage(getPrefix(sender) + message);
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param sender  the sender.
     * @param message the message to send.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(getPrefix(sender) + message);
    }

    /**
     * Sends a message to the sender with the plugin prefix.
     *
     * @param sender       the sender.
     * @param message      the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String message, Object... replacements) {
        sender.sendMessage(getPrefix(sender) + message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param key the key of the message to send.
     */
    public void sendRawMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key) {
        final String message = formatMessage(sender, MessageType.INFO, key);
        sender.sendMessage(message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender       the sender.
     * @param key          the key of the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendRawMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        final String message = formatMessage(sender, MessageType.INFO, key, replacements);
        sender.sendMessage(message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender  the sender.
     * @param message the message to send.
     */
    public void sendRawMessage(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender       the sender.
     * @param message      the message to send.
     * @param replacements the replacements to apply to the message.
     */
    public void sendRawMessage(@NotNull CommandSender sender, @NotNull String message, Object... replacements) {
        sender.sendMessage(message);
    }

    /**
     * Gets a message for the sender with at the given key.
     *
     * @param sender the sender.
     * @param key the key of the message to get.
     * @return the message.
     */
    @NotNull
    public String getMessage(@Nullable CommandSender sender, @NotNull MessageKeyProvider key) {
        final MessageKey msgKey = key.getMessageKey();
        String message = localeManager.getMessage(sender, msgKey);
        if (message == null) {
            PluginLogger.info("MISSING LANGUAGE KEY: " + msgKey.getKey());
            message = "<MISSING_LANGUAGE_KEY: " + msgKey.getKey() + ">";
        }
        return message;
    }

    /**
     * Replaces I18N strings in the given message.
     *
     * @param message the message to replace I18N strings in.
     * @return the message with the I18N strings replaced.
     */
    @Contract("!null -> !null")
    public String replaceI18NStrings(final String message) {
        if (message == null)
            return null;
        final Matcher matcher = PLCommonsPatterns.I18N_STRING.matcher(message);
        if (!matcher.find())
            return message;
        final BukkitCommandIssuer issuer = (BukkitCommandIssuer) CommandManager.getCurrentCommandIssuer();
        final CommandSender sender = issuer.getIssuer();

        matcher.reset();
        final StringBuffer sb = new StringBuffer(message.length());
        while (matcher.find()) {
            final MessageKey key = MessageKey.of(matcher.group("key"));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(getMessage(sender, key)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Formats a message as an info message.
     *
     * @param sender the sender.
     * @param key the key of the message.
     * @param replacements the replacements to apply to the message.
     * @return the formatted message.
     */
    @NotNull
    public String formatMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        String message = getMessage(sender, key);
        if (replacements.length > 0)
            message = replaceStrings(message, replacements);
        final PLCommonsMessageFormatter formatter = messageFormatters.getOrDefault(MessageType.INFO, this.defaultFormatter);
        if (formatter != null)
            message = formatter.format(message);
        return message;
    }

    /**
     * Formats a message.
     *
     * @param sender the sender.
     * @param type the message type.
     * @param key the key of the message.
     * @param replacements the replacements to apply to the message.
     * @return the formatted message.
     */
    @NotNull
    public String formatMessage(@NotNull CommandSender sender, @NotNull MessageType type, @NotNull MessageKeyProvider key, String... replacements) {
        String message = getMessage(sender, key);
        if (replacements.length > 0)
            message = replaceStrings(message, replacements);
        final PLCommonsMessageFormatter formatter = messageFormatters.getOrDefault(type, this.defaultFormatter);
        if (formatter != null)
            message = formatter.format(message);
        return message;
    }

    /**
     * Performs the given replacements on the string.
     *
     * @param string the string to perform replacements on.
     * @param replacements the replacements to perform, as key-value pairs.
     * @return the string with the replacements performed.
     * @throws IllegalArgumentException if the replacements are not in pairs of two.
     */
    @NotNull
    private String replaceStrings(@NotNull String string, String... replacements) {
        if (replacements.length < 2 || replacements.length % 2 != 0)
            throw new IllegalArgumentException("Replacements must be in pairs of two.");

        for (int i = 0; i < replacements.length; i += 2) {
            final String key = "%" + replacements[i] + "%";
            String value = replacements[i + 1];
            if (value == null)
                value = "";
            string = replace(string, key, value);
        }
        return string;
    }

    /**
     * Replaces the given pattern with the replacement in the given string.
     *
     * @param string the string to replace in.
     * @param pattern the pattern to replace.
     * @param replacement the replacement.
     * @return the string with the pattern replaced.
     */
    private @NotNull String replace(@NotNull String string, @NotNull String pattern, @NotNull String replacement) {
        return replace(string, PLCommonsPatterns.getPattern(Pattern.quote(pattern)), replacement);
    }

    /**
     * Replaces the given pattern with the replacement in the given string.
     *
     * @param string the string to replace in.
     * @param pattern the pattern to replace.
     * @param replacement the replacement.
     * @return the string with the pattern replaced.
     */
    private @NotNull String replace(@NotNull String string, @NotNull Pattern pattern, @NotNull String replacement) {
        return pattern.matcher(string).replaceAll(Matcher.quoteReplacement(replacement));
    }

    /**
     * Gets the plugin prefix for the sender.
     *
     * @param sender the sender.
     * @return the plugin prefix.
     */
    private @NotNull String getPrefix(@NotNull CommandSender sender) {
        if (pluginPrefix == null) {
            String prefix = getMessage(sender, PLCommonsMessageKeys.PLUGIN_PREFIX);
            final PLCommonsMessageFormatter formatter = messageFormatters.getOrDefault(MessageType.INFO, this.defaultFormatter);
            prefix = formatter.format(prefix);
            final String def = formatter.format(0, "");
            pluginPrefix = prefix + def + " ";
        }
        return pluginPrefix;
    }

    /**
     * Gets the UUID of the sender.
     *
     * @param sender the sender.
     * @return the UUID of the sender.
     */
    private @NotNull UUID getUniqueId(@NotNull CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.nameUUIDFromBytes(sender.getName().getBytes(StandardCharsets.UTF_8));
    }
}
