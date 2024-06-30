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

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Class for storing the plugin instance using the current instance of the library.
 */
final class PLCommons {

    private static Plugin pluginInstance;

    /**
     * Sets the plugin instance.
     *
     * @param pluginInstance the plugin instance.
     */
    static void setPluginInstance(@NotNull Plugin pluginInstance) {
        PLCommons.pluginInstance = pluginInstance;
    }

    /**
     * Returns the plugin instance.
     *
     * @return the plugin instance
     * @throws IllegalStateException if this method is called before the plugin instance has been set internally.
     */
    @NotNull
    public static Plugin getPluginInstance() {
        if (pluginInstance == null)
            throw new IllegalStateException("Plugin instance has not been set");
        return pluginInstance;
    }
}
