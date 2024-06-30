package co.aikar.commands;

import dev.greenadine.plcommons.PLCommonsLanguageManager;
import org.bukkit.plugin.Plugin;

public class ExtendedPaperCommandManager extends PaperCommandManager {

    private final PLCommonsLanguageManager languageManager;

    public ExtendedPaperCommandManager(Plugin plugin, PLCommonsLanguageManager languageManager) {
        super(plugin);
        this.languageManager = languageManager;
    }

    @Override
    public RootCommand createRootCommand(String cmd) {
        return new ExtendedBukkitRootCommand(this, cmd, languageManager);
    }
}
