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

package co.aikar.commands;

import dev.greenadine.plcommons.PLCommonsLanguageManager;
import org.bukkit.plugin.Plugin;

public class ExtendedBukkitCommandManager extends BukkitCommandManager {

    private final PLCommonsLanguageManager languageManager;

    public ExtendedBukkitCommandManager(Plugin plugin, PLCommonsLanguageManager languageManager) {
        super(plugin);
        this.languageManager = languageManager;
    }

    @Override
    public RootCommand createRootCommand(String cmd) {
        return new ExtendedBukkitRootCommand(this, cmd, languageManager);
    }
}
