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

import co.aikar.commands.*;
import co.aikar.locales.MessageKeyProvider;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Super class for Minecraft server platform-specific command managers.
 *
 * @param <CM>  the ACF command manager type.
 * @param <CON> the ACF command contexts type.
 * @param <COM> the ACF command completions type.
 */
public abstract class PLCommonsCommandManager<
        CM extends BukkitCommandManager,
        CON extends BukkitCommandContexts,
        COM extends BukkitCommandCompletions> {

    private final CM commandManager;
    private final PLCommonsLanguageManager languageManager;
    private MessageKeyProvider prefixMessageKey;

    private String messagePrefix;

    @SuppressWarnings("deprecation,unchecked")
    PLCommonsCommandManager(@NotNull CM commandManager, @NotNull PLCommonsLanguageManager languageManager) {
        this.commandManager = commandManager;
        this.languageManager = languageManager;

        // Configure ACF manager
        commandManager.enableUnstableAPI("help");
        registerContextResolvers((CON) commandManager.getCommandContexts());
        registerCommandConditions(commandManager.getCommandConditions());
        registerCommandCompletions((COM) commandManager.getCommandCompletions());
    }

    /**
     * Gets the underlying {@link CM command manager}.
     *
     * @return the underlying command manager.
     */
    public @NotNull CM getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the language manager.
     *
     * @return the language manager.
     */
    public @NotNull PLCommonsLanguageManager getLanguageManager() {
        return languageManager;
    }

    /**
     * Gets the prefix message key.
     *
     * @param key the prefix message key.
     */
    public void setPrefixMessageKey(@NotNull MessageKeyProvider key) {
        this.prefixMessageKey = key;
    }

    /**
     * Registers a command with the command manager.
     *
     * @param command the command to register.
     */
    public final void registerCommand(@NotNull PLCommonsCommand command) {
        commandManager.registerCommand(command);
        command.onRegister(languageManager);
    }

    /**
     * Gets the message prefix.
     *
     * @return the message prefix.
     */
    @Nullable
    String getMessagePrefix() {
        if (messagePrefix == null) {
            messagePrefix  = commandManager.getLocales().getMessage(commandManager.getCommandIssuer(null), prefixMessageKey);
            messagePrefix = BukkitUtils.formatColored(messagePrefix);
        }
        return messagePrefix;
    }

    /**
     * By default, this method does nothing. Can be overridden to register contexts resolvers. This method is called by the library and
     * should never be called directly.
     *
     * @param contexts the command contexts.
     */
    @OverrideOnly
    protected void registerContextResolvers(CON contexts) {
    }

    /**
     * By default, this method does nothing. Can be overridden to register command conditions. This method is called by the library and
     * should never be called directly.
     *
     * @param conditions the command conditions.
     */
    @OverrideOnly
    protected void registerCommandConditions(CommandConditions<BukkitCommandIssuer, BukkitCommandExecutionContext, BukkitConditionContext> conditions) {
    }

    /**
     * By default, this method does nothing. Can be overridden to register command completions. This method is called by the library and
     * should never be called directly.
     *
     * @param completions the command completions.
     */
    @OverrideOnly
    protected void registerCommandCompletions(COM completions) {
    }
}
