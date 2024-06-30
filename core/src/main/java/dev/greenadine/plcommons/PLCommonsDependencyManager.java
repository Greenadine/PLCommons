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

package dev.greenadine.plcommons;

import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.lib.util.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class PLCommonsDependencyManager {

    private final Table<Class<?>, String, Object> dependencies = new Table<>();

    /**
     * Gets a dependency with the specified key.
     *
     * @param clazz the class of the dependency.
     * @return the dependency with the specified key.
     * @throws IllegalStateException if the dependency is not found.
     */
    public <T> T getDependency(@NotNull Class<? extends T> clazz) {
        return getDependency(clazz, clazz.getName());
    }

    /**
     * Gets a dependency with the specified key.
     *
     * @param clazz the class of the dependency.
     * @param key   the key of the dependency.
     * @return the dependency with the specified key.
     * @throws IllegalStateException if the dependency is not found.
     */
    public <T> T getDependency(@NotNull Class<? extends T> clazz, @NotNull String key) {
        if (!dependencies.containsKey(clazz, key))
            throw new IllegalStateException("Dependency not found: " + clazz.getName() + " with key " + key);
        //noinspection unchecked
        return (T) dependencies.get(clazz, key);
    }

    /**
     * Checks if a dependency is present.
     *
     * @param clazz the class of the dependency.
     * @return {@code true} if the soft dependency is present, {@code false} otherwise.
     */
    public boolean checkSoftDependency(@NotNull Class<?> clazz) {
        return dependencies.containsKey(clazz, clazz.getName());
    }

    /**
     * Checks if a dependency is present.
     *
     * @param clazz the class of the dependency.
     * @param key   the key of the dependency.
     * @return {@code true} if the soft dependency is present, {@code false} otherwise.
     */
    public boolean checkSoftDependency(@NotNull Class<?> clazz, @NotNull String key) {
        return dependencies.containsKey(clazz, key);
    }

    /**
     * Registers a dependency.
     *
     * @param clazz    the class of the dependency.
     * @param instance the dependency instance.
     */
    public <T> void registerDependency(@NotNull Class<? extends T> clazz, @NotNull T instance) {
        registerDependency(clazz, clazz.getName(), instance);
    }

    /**
     * Registers a dependency with the specified key.
     *
     * @param clazz    the class of the dependency.
     * @param key      the key of the dependency.
     * @param instance the dependency instance.
     */
    public <T> void registerDependency(@NotNull Class<? extends T> clazz, @NotNull String key, @NotNull T instance) {
        if (dependencies.containsKey(clazz, key))
            throw new IllegalStateException("Dependency already registered: " + clazz.getName() + " with key " + key);
        dependencies.put(clazz, key, instance);
    }

    /**
     * Registers a dependency.
     *
     * @param clazz            the class of the dependency.
     * @param instanceSupplier the supplier for the instance of the dependency.
     * @throws IllegalStateException if an instance of the dependency is already registered.
     */
    public <T> void registerDependency(@NotNull Class<? extends T> clazz, @NotNull Supplier<T> instanceSupplier) {
        registerDependency(clazz, clazz.getName(), instanceSupplier);
    }

    /**
     * Registers a dependency with the specified key.
     *
     * @param clazz            the class of the dependency.
     * @param key              the key of the dependency.
     * @param instanceSupplier the supplier for the instance of the dependency.
     */
    public <T> void registerDependency(@NotNull Class<? extends T> clazz, @NotNull String key, @NotNull Supplier<T> instanceSupplier) {
        if (dependencies.containsKey(clazz, key))
            throw new IllegalStateException("Dependency already registered: " + clazz.getName() + " with key " + key);
        final T instance = instanceSupplier.get();
        if (instance == null)
            throw new NullPointerException("Required dependency is null: " + clazz.getName() + " with key " + key);
        dependencies.put(clazz, key, instance);
    }

    /**
     * Registers a soft dependency.
     *
     * @param clazz    the class of the dependency.
     * @param instance the dependency instance, may be {@code null}.
     */
    public <T> void registerSoftDependency(@NotNull Class<? extends T> clazz, @Nullable T instance) {
        registerSoftDependency(clazz, clazz.getName(), instance);
    }

    /**
     * Registers a soft dependency with the specified key.
     *
     * @param clazz    the class of the dependency.
     * @param key      the key of the dependency.
     * @param instance the dependency instance, may be {@code null}.
     */
    public <T> void registerSoftDependency(@NotNull Class<? extends T> clazz, @NotNull String key, @Nullable T instance) {
        if (instance == null)
            return;
        if (dependencies.containsKey(clazz, key))
            return;
        dependencies.put(clazz, key, instance);
    }

    /**
     * Registers a soft dependency.
     *
     * @param clazz            the class of the dependency.
     * @param instanceSupplier the supplier for the instance of the dependency, may return {@code null}.
     */
    public <T> void registerSoftDependency(@NotNull Class<? extends T> clazz, @NotNull Supplier<T> instanceSupplier) {
        registerSoftDependency(clazz, clazz.getName(), instanceSupplier);
    }

    /**
     * Registers a soft dependency with the specified key.
     *
     * @param clazz            the class of the dependency.
     * @param key              the key of the dependency.
     * @param instanceSupplier the supplier for the instance of the dependency, may return {@code null}.
     */
    public <T> void registerSoftDependency(@NotNull Class<? extends T> clazz, @NotNull String key,
                                           @NotNull Supplier<T> instanceSupplier) {
        if (dependencies.containsKey(clazz, key))
            return;
        final T instance = instanceSupplier.get();
        if (instance == null)
            return;
        dependencies.put(clazz, key, instance);
    }

    /**
     * Unregisters a dependency.
     *
     * @param clazz the class of the dependency.
     * @throws IllegalStateException if no registered dependency was found.
     */
    public <T> void unregisterDependency(@NotNull Class<? extends T> clazz) {
        unregisterDependency(clazz, clazz.getName());
    }

    /**
     * Unregisters a dependency with the given key.
     *
     * @param clazz the class of the dependency.
     * @param key   the key of the dependency.
     * @throws IllegalStateException if no registered dependency was found.
     */
    public <T> void unregisterDependency(@NotNull Class<? extends T> clazz, @NotNull String key) {
        if (!dependencies.containsKey(clazz, key))
            throw new IllegalStateException("Dependency not registered: " + clazz.getName());
        dependencies.remove(clazz, key);
    }

    /**
     * Injects dependencies into the specified object.
     *
     * @param object the object to inject dependencies into.
     */
    public void injectDependencies(@NotNull Object object) {
        final Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Dependency.class)) {
                final String dependency = field.getAnnotation(Dependency.class).value();
                String key = dependency.isEmpty() ? field.getType().getName() : dependency;
                final Object value = dependencies.get(field.getType(), key);
                if (value == null)
                    throw new UnresolvedDependencyException("Dependency not found: " + field.getType().getName() + " with key " + key +
                            " for field " + field.getName() +
                            " in class " + object.getClass().getName());
                try {
                    boolean accessible = field.isAccessible();
                    if (!accessible)
                        field.setAccessible(true);
                    field.set(object, value);
                    field.setAccessible(accessible);
                } catch (IllegalAccessException ex) {
                    throw new UnresolvedDependencyException("Failed to inject dependency: " + field.getType().getName() + " with key " + key + " for field " + field.getName() +
                            " in class " + object.getClass().getName(), ex);
                }
            }
        }
    }
}
