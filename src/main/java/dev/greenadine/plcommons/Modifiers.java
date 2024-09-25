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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * A modifier is an object that holds a list of consumers that can be applied to a target object. This is useful for applying the same set of modifications to multiple objects. All modifiers
 * are applied in the order they were added. The modifiers can be appended, prepended, removed, or cleared.
 * <p>
 * It is not related to/should not be confused with Java's {@link java.lang.reflect.Modifier} class.
 *
 * @param <T> the target type.
 * @since 0.1
 */
public class Modifiers<T> {

    private final ArrayList<Consumer<T>> modifiers;

    /**
     * Creates a new Modifiers object with an initial capacity of 10.
     */
    public Modifiers() {
        modifiers = new ArrayList<>();
    }

    /**
     * Creates a new Modifiers object with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity.
     */
    public Modifiers(int initialCapacity) {
        modifiers = new ArrayList<>(initialCapacity);
    }

    /**
     * Adds a modifier to the end of the list.
     *
     * @param modifier the modifier.
     */
    public void append(Consumer<T> modifier) {
        modifiers.add(modifier);
    }

    /**
     * Adds multiple modifiers to the end of the list.
     *
     * @param modifiers the modifiers.
     */
    public void appendAll(@NotNull Collection<Consumer<T>> modifiers) {
        modifiers.forEach(this::append);
    }

    /**
     * Adds a modifier to the beginning of the list.
     *
     * @param modifier the modifier.
     */
    public void prepend(Consumer<T> modifier) {
        modifiers.add(0, modifier);
    }

    /**
     * Adds multiple modifiers to the beginning of the list.
     *
     * @param modifiers the modifiers.
     */
    public void prependAll(@NotNull Collection<Consumer<T>> modifiers) {
        modifiers.forEach(this::prepend);
    }

    /**
     * Inserts a modifier at the specified index.
     *
     * @param index    the index.
     * @param modifier the modifier.
     */
    public void insert(int index, Consumer<T> modifier) {
        modifiers.add(index, modifier);
    }

    /**
     * Inserts multiple modifiers at the specified index.
     *
     * @param index     the index.
     * @param modifiers the modifiers.
     */
    public void insertAll(int index, @NotNull Collection<Consumer<T>> modifiers) {
        for (final Consumer<T> modifier : modifiers)
            insert(index++, modifier);
    }

    /**
     * Removes a modifier from the list.
     *
     * @param modifier the modifier.
     */
    public void remove(Consumer<T> modifier) {
        modifiers.remove(modifier);
    }

    /**
     * Removes a modifier from the list.
     *
     * @param modifiers the modifiers.
     */
    public void removeAll(@NotNull Collection<Consumer<T>> modifiers) {
        this.modifiers.removeAll(modifiers);
    }

    /**
     * Clears all modifiers from the list.
     */
    public void clear() {
        modifiers.clear();
    }

    /**
     * Applies all modifiers to the target. If the target is {@code null}, nothing will happen.
     *
     * @param target the target.
     */
    public void apply(T target) {
        if (target == null)
            return;
        modifiers.forEach(modifier -> modifier.accept(target));
    }
}
