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

package co.aikar.commands;

import dev.greenadine.plcommons.PLCLanguageManager;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of {@link BukkitRootCommand} that overrides default description behavior.
 */
@Internal
public class ExtendedBukkitRootCommand extends BukkitRootCommand {

    private final PLCLanguageManager languageManager;

    public ExtendedBukkitRootCommand(BukkitCommandManager commandManager, String name, PLCLanguageManager languageManager) {
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
