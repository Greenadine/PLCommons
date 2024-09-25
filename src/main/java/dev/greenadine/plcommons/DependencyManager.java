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

import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.lib.util.Table;
import com.google.common.base.Preconditions;
import dev.greenadine.plcommons.exception.UnresolvedDependencyException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Manager class for dependency injection. Can be linked to an {@link ExtendedCommandManager} to use the same dependencies table.
 *
 * @since 0.1
 */
public class DependencyManager {

    private Table<Class<?>, String, Object> dependencies;
    private final ArrayList<Class<?>> excludedTypes = new ArrayList<>();

    DependencyManager() {
        this.dependencies = new Table<>();
        excludeType(PLCommonsCommand.class);
        excludeType(ConfigurationSerializable.class);
    }

    /**
     * Switches the dependency manager to use the dependencies table of the provided command manager. This adds all dependencies that were already registered to the command manager's
     * dependencies table.
     *
     * @param commandManager the command manager to use the dependencies table of.
     */
    void useCommandManagerDependencies(@NotNull ExtendedCommandManager commandManager) {
        dependencies.forEach(commandManager.getDependenciesTable()::put);
        this.dependencies = commandManager.getDependenciesTable();
    }

    /**
     * Registers a dependency.
     *
     * @param type     the type of the object to serialize and deserialize.
     * @param instance the dependency instance.
     * @param <T>      the type of the object to serialize and deserialize.
     */
    public <T> void registerDependency(@NotNull Class<T> type, @NotNull T instance) {
        registerDependency(type, type.getName(), instance);
    }

    /**
     * Registers a dependency.
     *
     * @param type     the type of the object to serialize and deserialize.
     * @param key      the key to register the dependency under.
     * @param instance the dependency instance.
     * @param <T>      the type of the object to serialize and deserialize.
     */
    public <T> void registerDependency(@NotNull Class<T> type, @NotNull String key, @NotNull T instance) {
        Preconditions.checkNotNull(type, "Type cannot be null");
        Preconditions.checkNotNull(instance, "instance cannot be null");
        dependencies.put(type, key, instance);
    }

    /**
     * Excludes a type from dependency injection, meaning injection will not be performed on fields of this type, or on any of its superclasses.
     *
     * @param type the type to exclude.
     */
    public void excludeType(@NotNull Class<?> type) {
        Preconditions.checkNotNull(type, "Type cannot be null");
        excludedTypes.add(type);
    }

    /**
     * Gets a dependency by its type.
     *
     * @param type the type of the dependency.
     * @param <T>  the type of the dependency.
     * @return the dependency.
     * @throws NullPointerException  if the type is null.
     * @throws IllegalStateException if no supplier is found for the dependency type.
     */
    public <T> @NotNull T getDependency(@NotNull Class<T> type, @NotNull String key) {
        Preconditions.checkNotNull(type, "Type cannot be null");
        Preconditions.checkNotNull(key, "Key cannot be null");

        final Object obj = dependencies.row(type).get(key);
        if (obj == null)
            throw new IllegalArgumentException("No dependency found for type " + type.getName() + " with key " + key);
        return type.cast(obj);
    }

    /**
     * Injects dependencies into an object.
     *
     * @param obj the object to inject dependencies into.
     * @param <T> the type of the object.
     */
    public <T> void injectDependencies(@NotNull Object obj) {
        Preconditions.checkNotNull(obj, "Object cannot be null");

        Class clazz = obj.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Dependency.class))
                    continue;

                final Class<?> type = field.getType();
                final String annValue = field.getAnnotation(Dependency.class).value();
                final String key = annValue.isEmpty() ? field.getType().getName() : annValue;
                final Object dependency = dependencies.row(type).get(key);
                if (dependency == null) {
                    PluginLogger.warn("No dependency found for field '" + type.getName() + " " + field.getName() + "' with key '" + key + "'");
                    continue;
                }

                final boolean accessible = field.isAccessible();
                try {
                    if (!accessible)
                        field.setAccessible(true);
                    field.set(obj, dependency);
                } catch (IllegalAccessException ex) {
                    final UnresolvedDependencyException dependencyException = new UnresolvedDependencyException("Could not set value of field " + field.getName() + ": no access", ex);
                    PluginLogger.severe("Failed to inject dependency into field '" + type.getName() + " " + field.getName() + "'", dependencyException);
                } finally {
                    field.setAccessible(accessible);
                }
            }
            clazz = clazz.getSuperclass();
        } while (!excludedTypes.contains(clazz));
    }
}
