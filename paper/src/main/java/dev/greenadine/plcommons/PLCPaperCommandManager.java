package dev.greenadine.plcommons;

import co.aikar.commands.PaperCommandCompletions;
import co.aikar.commands.PaperCommandContexts;
import co.aikar.commands.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

/**
 * PLCommons command manager for Paper servers.
 *
 * @since 0.1
 */
public class PLCPaperCommandManager
        extends PLCommonsCommandManager<
            PaperCommandManager,
            PaperCommandContexts,
            PaperCommandCompletions> {

    public PLCPaperCommandManager(@NotNull PLCommonsLanguageManager languageManager) {
        super(new PaperCommandManager(PLCommons.getPluginInstance()), languageManager);
    }
}
