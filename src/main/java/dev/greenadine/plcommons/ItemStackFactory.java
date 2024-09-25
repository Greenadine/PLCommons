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

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * A utility class for creating {@link ItemStack}s in a more fluent way.
 *
 * @since 0.1
 */
public class ItemStackFactory {

    private final Material material;
    private final Modifiers<ItemMeta> metaModifiers;
    private short durability = -1;

    ItemStackFactory(@NotNull Material material) {
        this.material = material;
        this.metaModifiers = new Modifiers<>();
    }

    /**
     * Creates a new ItemFactory for an item stack with the given material.
     *
     * @param material the material.
     * @return the {@code ItemFactory} instance.
     */
    public static @NotNull ItemStackFactory with(@NotNull Material material) {
        return new ItemStackFactory(material);
    }

    /**
     * Sets the name of the item.
     *
     * @param name the name.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setName(@NotNull String name) {
        metaModifiers.append(meta -> meta.setDisplayName(name));
        return this;
    }

    /**
     * Sets the name of the item with color codes support.
     *
     * @param name the name.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setNameColored(@NotNull String name) {
        metaModifiers.append(meta -> meta.setDisplayName(Strings.colorize(name)));
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore the lore.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setLore(@Nullable String... lore) {
        Preconditions.checkArgument(lore.length > 0, "Lore cannot be empty.");
        metaModifiers.append(meta -> meta.setLore(Arrays.asList(lore)));
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore the lore.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setLore(@NotNull List<String> lore) {
        Preconditions.checkArgument(!lore.isEmpty(), "Lore cannot be empty");
        metaModifiers.append(meta -> meta.setLore(lore));
        return this;
    }

    /**
     * Sets the lore of the item with color codes support.
     *
     * @param lore the lore.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setLoreColored(@NotNull String... lore) {
        Preconditions.checkNotNull(lore, "Lore cannot be null");
        Preconditions.checkArgument(lore.length > 0, "Lore cannot be empty");

        metaModifiers.append(meta -> {
            for (int i = 0; i < lore.length; i++) {
                lore[i] = Strings.colorize(lore[i]);
            }
            meta.setLore(Arrays.asList(lore));
        });
        return this;
    }

    /**
     * Sets the lore of the item with color codes support.
     *
     * @param lore the lore.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setLoreColored(@NotNull List<String> lore) {
        Preconditions.checkArgument(!lore.isEmpty(), "Lore cannot be empty");

        metaModifiers.append(meta -> {
            lore.replaceAll(Strings::colorize);
            meta.setLore(lore);
        });
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability the durability.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setDurability(short durability) {
        if (BukkitVersion.isAtMost(BukkitVersion.V1_12))
            this.durability = durability;
        else
            metaModifiers.append(meta -> ((org.bukkit.inventory.meta.Damageable) meta).setDamage(durability));
        return this;
    }

    /**
     * Sets whether the item is unbreakable.
     *
     * @param unbreakable the unbreakable state.
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory setUnbreakable(boolean unbreakable) {
        metaModifiers.append(meta -> meta.setUnbreakable(unbreakable));
        return this;
    }

    /**
     * Adds the given flag(s) to the item.
     *
     * @param flags the flag(s).
     * @return the {@code ItemFactory} instance.
     */
    public @NotNull ItemStackFactory addFlags(@NotNull ItemFlag... flags) {
        Preconditions.checkArgument(flags.length > 0, "Flags cannot be empty.");
        metaModifiers.append(meta -> meta.addItemFlags(flags));
        return this;
    }

    /**
     * Creates and returns the final item stack.
     *
     * @return the final item stack.
     */
    public @NotNull ItemStack build() {
        return build(1);
    }

    /**
     * Creates and returns the final item stack.
     *
     * @param amount the amount of items in the stack.
     * @return the final item stack.
     */
    public @NotNull ItemStack build(int amount) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            PluginLogger.severe("[ItemFactory] Failed to set item meta: ItemMeta is null for material " + material);
            return item;
        }
        // Set durability for 1.12 and prior
        if (durability != -1)
            //noinspection deprecation
            item.setDurability(durability);

        metaModifiers.apply(meta);
        item.setItemMeta(meta);
        return item;
    }
}
