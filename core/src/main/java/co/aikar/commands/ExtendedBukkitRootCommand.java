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
import org.jetbrains.annotations.NotNull;

public class ExtendedBukkitRootCommand extends BukkitRootCommand {

    private final PLCommonsLanguageManager languageManager;

    ExtendedBukkitRootCommand(BukkitCommandManager commandManager, String name, PLCommonsLanguageManager languageManager) {
        super(commandManager, name);
        this.languageManager = languageManager;
    }

    @Override
    public @NotNull String getDescription() {
        final RegisteredCommand<?> command = getDefaultRegisteredCommand();
        String description = null;

        if (command != null && !command.getHelpText().isEmpty())
            description = command.getHelpText();
        else if (command != null && command.scope.description != null)
            description = command.scope.description;

        if (description != null)
            return languageManager.replaceI18NStrings(description);
        return super.getDescription();
    }
}
