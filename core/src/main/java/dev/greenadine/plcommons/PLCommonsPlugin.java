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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An extension of {@link JavaPlugin} that provides some common functionality for Minecraft plugins.
 *
 * @since 0.1
 */
public abstract class PLCommonsPlugin extends JavaPlugin {

    /**
     * Called when the plugin is loaded, but before it is enabled.
     */
    protected void onPluginLoad() {
    }

    /**
     * Called when the plugin is enabled.
     */
    protected void onPluginEnable() throws PluginEnableException {
    }

    // -- JavaPlugin Implementation --

    @Override
    public final void onLoad() {
        try {
            validatePackageLocation();
            onPluginLoad();
        } catch (Throwable thrown) {
            shutdown(null, thrown);
        }
    }

    @Override
    public final void onEnable() {
        try {
            PLCommons.setPluginInstance(this);
            onPluginEnable();
        } catch (PluginEnableException ex) {
            shutdown(ex.messageLines, ex);
        } catch (Throwable thrown) {
            shutdown(null, thrown);
        }
    }

    // -- Public Utility --

    /**
     * Registers the given listener.
     *
     * @param listener the listener to register.
     */
    protected void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers the given listeners.
     *
     * @param listeners the listeners to register.
     */
    protected void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(this::registerListener);
    }

    // -- Internal Utility --

    /**
     * Returns the name of the plugin with its version.
     *
     * @return the name of the plugin with its version.
     */
    @NotNull
    private String getNameWithVersion() {
        return getDescription().getName() + " " + getDescription().getVersion();
    }

    /**
     * Returns the prefix for fatal error messages.
     *
     * @return the prefix for fatal error messages
     */
    @NotNull
    private String getFatalErrorPrefix() {
        return ChatColor.DARK_RED + "[" + getDescription().getName() + "] " + ChatColor.RED;
    }

    /**
     * Validates the package location of the library, ensuring it is not present in the default package.
     *
     * @throws IllegalStateException if the library is present in the default package.
     */
    private void validatePackageLocation() {
        final String defaultPackage = "dev-greenadine-plcommons".replace("-", ".");  // Prevent Maven's Relocate from ruining this
        if (PLCommonsPlugin.class.getPackage().getName().equals(defaultPackage))
            throw new IllegalStateException("PLCommons is present in default package, should be relocated to another package to avoid conflicts with other plugins");
    }

    /**
     * Shuts down the plugin, logging the error message lines and the thrown exception.
     *
     * @param errorMessageLines the error message lines
     * @param thrown            the thrown exception
     */
    private void shutdown(@Nullable List<String> errorMessageLines, @Nullable Throwable thrown) {
        printError(errorMessageLines, thrown);

        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.getConsoleSender().sendMessage(getFatalErrorPrefix() + "A fatal error occurred while enabling the plugin. Check logs for more information."),
                10);

        setEnabled(false);
    }

    /**
     * Prints the error message lines and the thrown exception.
     *
     * @param errorMessageLines the error message lines
     * @param thrown            the thrown exception
     */
    private void printError(@Nullable List<String> errorMessageLines, @Nullable Throwable thrown) {
        final List<String> output = new ArrayList<>();

        if (errorMessageLines != null)
            output.add(getFatalErrorPrefix() + "Fatal error while enabling " + getNameWithVersion() + ":");
        else
            output.add(getFatalErrorPrefix() + "Fatal unexpected error while enabling " + getNameWithVersion() + ":");

        if (errorMessageLines != null) {
            output.add("");
            output.addAll(errorMessageLines);
        }
        if (thrown != null) {
            output.add("");
            output.addAll(ExceptionUtils.getStackTraceOutputLines(thrown));
            output.add("");
        }
        output.add("The plugin has been disabled.");
        output.add("");

        Bukkit.getConsoleSender().sendMessage(String.join("\n", output));
    }

    /**
     * An exception that can be thrown inside {@link #onPluginEnable()} to indicate an error during plugin enable.
     */
    public static class PluginEnableException extends Exception {

        private final List<String> messageLines;

        /**
         * Constructs a new plugin enable exception with the given message.
         *
         * @param message the message.
         */
        public PluginEnableException(@Nullable String... message) {
            this(null, message);
        }

        /**
         * Constructs a new plugin enable exception with the given message and cause.
         *
         * @param cause the cause.
         * @param message the message.
         */
        public PluginEnableException(@Nullable Throwable cause, @Nullable String... message) {
            super(String.join(" ", message), cause);
            this.messageLines = Arrays.asList(message);
        }
    }
}
