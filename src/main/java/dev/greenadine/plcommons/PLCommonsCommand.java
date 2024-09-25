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

import co.aikar.commands.*;
import co.aikar.commands.ExtendedBaseCommand;
import org.jetbrains.annotations.NotNull;

/**
 * Super class for plugin commands. Should be registered using a {@link PLCCommandManager}.
 *
 * @since 0.1
 */
public abstract class PLCommonsCommand extends ExtendedBaseCommand {

    private PLCLanguageManager languageManager;
    private String prefix;

    @Override
    public void showSyntax(CommandIssuer issuer, RegisteredCommand<?> cmd) {
        languageManager.sendMessage(issuer.getIssuer(), MessageType.SYNTAX, PLCMessageKeys.COMMAND_INVALID_SYNTAX, true,"command", getCurrentCommandManager().getCommandPrefix(issuer) + cmd.getCommand(), "syntax", cmd.getSyntaxText(issuer));
    }

    /**
     * Called when the command is registered.
     *
     * @param languageManager the PLCommons language manager.
     */
    void onRegister(@NotNull PLCLanguageManager languageManager) {
        this.languageManager = languageManager;
    }
}
