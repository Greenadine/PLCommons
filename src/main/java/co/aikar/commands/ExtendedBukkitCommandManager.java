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

import co.aikar.commands.lib.util.Table;
import dev.greenadine.plcommons.ExtendedCommandManager;
import dev.greenadine.plcommons.PLCommons;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * Extension of {@link BukkitCommandManager} that exposes the dependencies table and overrides dependency injection behavior.
 */
@Internal
public class ExtendedBukkitCommandManager extends BukkitCommandManager implements ExtendedCommandManager {

    public ExtendedBukkitCommandManager(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    void injectDependencies(BaseCommand baseCommand) {
        PLCommons.getDependencyManager().injectDependencies(baseCommand);
    }

    /**
     * Expose the dependencies table for usage in other classes. This is to avoid having multiple locations for dependency management.
     *
     * @return the dependencies table.
     */
    public @NotNull Table<Class<?>, String, Object> getDependenciesTable() {
        return dependencies;
    }
}
