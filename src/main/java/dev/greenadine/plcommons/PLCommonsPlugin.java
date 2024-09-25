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

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
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
    @OverrideOnly
    protected void onPluginLoad() {
    }

    /**
     * Called when the plugin is enabled.
     */
    @OverrideOnly
    protected void onPluginEnable() throws PluginEnableException {
    }

    /**
     * Handles an exception that occurs. This can be overridden to provide custom handling.
     */
    protected void handleThrown(@NotNull Throwable thrown) {
        PluginLogger.severe(thrown.getMessage(), thrown);
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
            PLCommons.pluginInstance = this;
            onPluginEnable();
        } catch (PluginEnableException ex) {
            shutdown(ex.messageLines, ex);
        } catch (Throwable thrown) {
            shutdown(null, thrown);
        }
    }

    // -- Public Utility --

    /**
     * Copies the resource with the given path to the plugin's data folder.
     *
     * @param paths the paths of the resource to copy.
     */
    public void copyResources(@NotNull final String... paths) {
        for (final String path : paths)
            if (!new File(getDataFolder(), path).exists())
                saveResource(path, false);
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
                PluginLogger.severe("A fatal error occurred while enabling the plugin. Check logs for more information."),
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
            output.add("Fatal error while enabling " + getNameWithVersion() + ":");
        else
            output.add("Fatal unexpected error while enabling " + getNameWithVersion() + ":");

        if (errorMessageLines != null) {
            output.add("");
            output.addAll(errorMessageLines);
        }
        if (thrown != null) {
            output.add("");
            output.addAll(Exceptions.getStackTraceOutputLines(thrown));
            output.add("");
        }
        output.add("The plugin has been disabled.");
        output.add("");
        PluginLogger.severe(String.join("\n", output));
    }
}
