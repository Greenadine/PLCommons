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

/**
 * A manager that handles language localization for plugins.
 * <p>
 * Language keys are stored in message bundles, which are loaded from the plugin's resources. For full documentation see Aikar's Locales library on GitHub.
 * </p>
 *
 * @since 0.1
 */
public class PLCLanguageManager {

    private final LocaleManager<CommandSender> localeManager;
    private final Set<Locale> supportedLanguages = new HashSet<>();
    private final Map<MessageType, PLCMessageFormatter> messageFormatters = new IdentityHashMap<>();
    private PLCMessageFormatter defaultFormatter;
    private String pluginPrefix;

    /**
     * Creates a new language manager supporting only a single locale.
     *
     * @param locale the locale.
     */
    public PLCLanguageManager(@NotNull Locale locale) {
        this.localeManager = LocaleManager.create((sender -> locale), locale);
        this.supportedLanguages.add(locale);
        setDefaultFormatters();
    }

    /**
     * Creates a new language manager with per-sender locale support.
     *
     * @param localeMapper       the sender locale mapper function.
     * @param defaultLocale      the default locale.
     * @param supportedLanguages the additionally supported languages (excl. default).
     */
    public PLCLanguageManager(@NotNull Function<CommandSender, Locale> localeMapper, @Nullable Locale defaultLocale,
                              Locale... supportedLanguages) {
        this.localeManager = LocaleManager.create(localeMapper, defaultLocale);
        this.supportedLanguages.add(defaultLocale);
        this.supportedLanguages.addAll(Arrays.asList(supportedLanguages));
        setDefaultFormatters();
    }

    /**
     * Sets default message formats.
     */
    private void setDefaultFormatters() {
        // TODO: Create language manager for Paper that uses NamedTextColor instead
        setDefaultFormat(ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.GOLD, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED);
        setFormat(MessageType.INFO, ChatColor.WHITE, ChatColor.GOLD, ChatColor.AQUA, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED);
        setFormat(MessageType.HELP, ChatColor.WHITE, ChatColor.GOLD, ChatColor.AQUA, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.RED);
        setFormat(MessageType.SYNTAX, ChatColor.GRAY, ChatColor.YELLOW);
        setFormat(MessageType.ERROR, ChatColor.RED, ChatColor.GOLD, ChatColor.AQUA, ChatColor.DARK_GRAY, ChatColor.GRAY);
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
     * @return {@code true} if the message bundle was added, {@code false} otherwise.
     */
    public boolean addMessageBundle(@NotNull String bundleName) {
        localeManager.addMessageBundle("PLCommons", supportedLanguages.toArray(new Locale[0]));
        return localeManager.addMessageBundle(bundleName, supportedLanguages.toArray(new Locale[0]));
    }

    /**
     * Sets the default message format.
     *
     * @param colors the colors to use in the default message format.
     */
    public void setDefaultFormat(ChatColor... colors) {
        defaultFormatter = new PLCMessageFormatter(colors);
    }

    /**
     * Sets the message format for the given type.
     *
     * @param type   the message type.
     * @param colors the colors to use in the message format.
     */
    public void setFormat(@NotNull MessageType type, @NotNull ChatColor... colors) {
        messageFormatters.put(type, new PLCMessageFormatter(colors));
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender the sender.
     * @param type   the message type.
     * @param key    the key of the message to send.
     * @param prefix {@code true} if the message should have the plugin prefix, {@code false} otherwise.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull MessageType type, @NotNull MessageKeyProvider key, boolean prefix) {
        final String message = formatMessage(sender, type, key);
        sender.sendMessage((prefix ? getPrefix(sender) : "") + message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender       the sender.
     * @param type         the message type.
     * @param key          the key of the message to send.
     * @param prefix       {@code true} if the message should have the plugin prefix, {@code false} otherwise.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull MessageType type, @NotNull MessageKeyProvider key, boolean prefix, String... replacements) {
        final String message = formatMessage(sender, type, key, replacements);
        sender.sendMessage((prefix ? getPrefix(sender) : "") + message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender  the sender.
     * @param message the message to send.
     * @param prefix  {@code true} if the message should have the plugin prefix, {@code false} otherwise.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String message, boolean prefix) {
        message = Strings.colorize(message);
        sender.sendMessage((prefix ? getPrefix(sender) : "") + message);
    }

    /**
     * Sends a message to the sender.
     *
     * @param sender       the sender.
     * @param message      the message to send.
     * @param prefix       {@code true} if the message should have the plugin prefix, {@code false} otherwise.
     * @param replacements the replacements to apply to the message.
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String message, boolean prefix, String... replacements) {
        message = Strings.colorize(message);
        if (replacements.length > 0)
            message = replaceStrings(message, replacements);
        sender.sendMessage((prefix ? getPrefix(sender) : "") + message);
    }

    /**
     * Gets a message for the sender with at the given key.
     *
     * @param sender the sender.
     * @param key    the key of the message to get.
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
        final Matcher matcher = PLCPatterns.I18N_STRING.matcher(message);
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
     * Formats an info message.
     *
     * @param sender       the sender.
     * @param key          the key of the message.
     * @param replacements the replacements to apply to the message.
     * @return the formatted message.
     */
    @NotNull
    public String formatMessage(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        return formatMessage(sender, MessageType.INFO, key, replacements);
    }

    /**
     * Formats a message.
     *
     * @param sender       the sender.
     * @param type         the message type.
     * @param key          the key of the message.
     * @param replacements the replacements to apply to the message.
     * @return the formatted message.
     */
    @NotNull
    public String formatMessage(@NotNull CommandSender sender, @NotNull MessageType type, @NotNull MessageKeyProvider key,
                                String... replacements) {
        String message = getMessage(sender, key);
        if (replacements.length > 0)
            message = replaceStrings(message, replacements);
        final PLCMessageFormatter formatter = messageFormatters.getOrDefault(type, this.defaultFormatter);
        if (formatter != null)
            message = formatter.format(message);
        return message;
    }

    /**
     * Formats an info message with alternate formatting, using '&' instead of the default color format ('{@code {<code>:<message>}}').
     *
     * @param sender       the sender.
     * @param key          the key of the message.
     * @param replacements the replacements to apply to the message.
     * @return the formatted message.
     */
    @NotNull
    public String formatMessageAlt(@NotNull CommandSender sender, @NotNull MessageKeyProvider key, String... replacements) {
        return formatMessageAlt(sender, MessageType.INFO, key, replacements);
    }

    /**
     * Formats a message with alternate formatting, using '&' instead of the default color format ('{@code {<code>:<message>}}').
     *
     * @param sender       the sender.
     * @param type         the message type.
     * @param key          the key of the message.
     * @param replacements the replacements to apply to the message.
     * @return the formatted message.
     */
    @NotNull
    public String formatMessageAlt(@NotNull CommandSender sender, @NotNull MessageType type, @NotNull MessageKeyProvider key,
                                   String... replacements) {
        String message = getMessage(sender, key);
        if (replacements.length > 0)
            message = replaceStrings(message, replacements);
        final PLCMessageFormatter formatter = messageFormatters.getOrDefault(type, this.defaultFormatter);
        if (formatter != null)
            message = formatter.formatAlt(message);
        return message;
    }

    /**
     * Performs the given replacements on the string.
     *
     * @param string       the string to perform replacements on.
     * @param replacements the replacements to perform, as key-value pairs.
     * @return the string with the replacements performed.
     * @throws IllegalArgumentException if the replacements are not in pairs of two.
     */
    @NotNull
    public String replaceStrings(@NotNull String string, String... replacements) {
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
     * @param string      the string to replace in.
     * @param pattern     the pattern to replace.
     * @param replacement the replacement.
     * @return the string with the pattern replaced.
     */
    private @NotNull String replace(@NotNull String string, @NotNull String pattern, @NotNull String replacement) {
        return replace(string, PLCPatterns.getPattern(Pattern.quote(pattern)), replacement);
    }

    /**
     * Replaces the given pattern with the replacement in the given string.
     *
     * @param string      the string to replace in.
     * @param pattern     the pattern to replace.
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
            String prefix = getMessage(sender, PLCMessageKeys.PLUGIN_PREFIX);
            final String pluginName = getMessage(sender, PLCMessageKeys.PLUGIN_NAME);
            prefix = replaceStrings(prefix, "plugin_name", pluginName);
            final PLCMessageFormatter formatter = messageFormatters.getOrDefault(MessageType.INFO, this.defaultFormatter);
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
        return sender instanceof Player ? ((Player) sender).getUniqueId() :
                UUID.nameUUIDFromBytes(sender.getName().getBytes(StandardCharsets.UTF_8));
    }
}
