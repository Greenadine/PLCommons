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

import dev.greenadine.plcommons.exception.InvalidConfigValueException;
import dev.greenadine.plcommons.exception.MissingConfigValueException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A utility class for common configuration operations, providing a more readable way of interacting with the config through functional programming.
 */
public class PLCConfig {

    private final FileConfiguration config = PLCommons.getPlugin().getConfig();
    private final Map<String, Object> values = new HashMap<>();

    /*
     * OBJECTS
     */

    /**
     * Gets the object at the specified path in the plugin configuration.
     *
     * @param path the path to the object.
     * @return the object at the specified path, or {@code null} if the specified path does not exist.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     */
    @Nullable
    public Object get(@NotNull String path) {
        if (!config.isSet(path))
            throw new MissingConfigValueException(path);
        return config.get(path);
    }

    /**
     * Gets the object at the specified path in the plugin configuration. If the object does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the object.
     * @param condition the condition for the configuration value.
     * @return the object at the specified path, or {@code null} if the specified path does not exist.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the object does not meet the specified condition.
     */
    @Nullable
    public Object get(@NotNull String path, @NotNull Predicate<Object> condition) {
        return get(path, condition, (String) null);
    }

    /**
     * Gets the object at the specified path in the plugin configuration. If the object does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown with the specified message.
     *
     * @param path           the path to the object.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the object at the specified path, or {@code null} if the specified path does not exist.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the object does not meet the specified condition.
     */
    @Nullable
    public Object get(@NotNull String path, @NotNull Predicate<Object> condition, @Nullable String detailsMessage) {
        final Object value = get(path);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Gets the object at the specified path in the plugin configuration, or returns the default value if the specified path does not exist.
     *
     * @param path the path to the object.
     * @param def  the default value.
     * @return the object at the specified path, or the default value if the specified path does not exist.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     */
    @Contract("_, !null -> !null")
    public Object get(@NotNull String path, @Nullable Object def) {
        return config.get(path, def);
    }

    /**
     * Gets the object at the specified path in the plugin configuration, or returns the default value if the specified path does not exist.
     * If the object does not meet the specified condition, an {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the object.
     * @param def       the default value.
     * @param condition the condition for the configuration value.
     * @return the object at the specified path, or the default value if the specified path does not exist.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the object does not meet the specified condition.
     */
    @Contract("_, !null, _ -> !null")
    public Object get(@NotNull String path, @Nullable Object def, @NotNull Predicate<Object> condition) {
        return get(path, def, condition, null);
    }

    /**
     * Gets the object at the specified path in the plugin configuration, or returns the default value if the specified path does not exist.
     * If the object does not meet the specified condition, an {@link InvalidConfigValueException} is thrown with the specified message.
     *
     * @param path           the path to the object.
     * @param def            the default value.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the object at the specified path, or the default value if the specified path does not exist.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the object does not meet the specified condition.
     */
    @Contract("_, !null, _, _ -> !null")
    public Object get(@NotNull String path, @Nullable Object def, @NotNull Predicate<Object> condition, @Nullable String detailsMessage) {
        final Object value = get(path, def);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /*
     * STRINGS
     */

    /**
     * Gets the string at the specified path in the plugin configuration.
     *
     * @param path the path to the string.
     * @return the string at the specified path, or {@code null} if the specified path does not exist or if its value is set to
     * {@code null}.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     */
    @Nullable
    public String getString(@NotNull String path) {
        return config.getString(path);
    }

    /**
     * Gets the string at the specified path in the plugin configuration. If the string does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the string.
     * @param condition the condition for the configuration value.
     * @return the string at the specified path, and otherwise {@code null} if the specified path does not exist, if its value is set to
     * {@code null}, or if the string does not meet the specified condition.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the string does not meet the specified condition.
     */
    @Nullable
    public String getString(@NotNull String path, @NotNull Predicate<String> condition) {
        return getString(path, condition, null);
    }

    /**
     * Gets the string at the specified path in the plugin configuration. If the string does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown.
     *
     * @param path           the path to the string.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the string at the specified path, and otherwise {@code null} if the specified path does not exist, if its value is set to
     * {@code null}, or if the string does not meet the specified condition.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the string does not meet the specified condition.
     */
    @Nullable
    public String getString(@NotNull String path, @NotNull Predicate<String> condition, @Nullable String detailsMessage) {
        final String value = getString(path);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Gets the string at the specified path in the plugin configuration. Will return the default value if the specified path does not exist
     * or if its value is set to {@code null}.
     *
     * @param path the path to the string.
     * @param def  the default value.
     * @return the string at the specified path, or the default value if the path is not found.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     */
    @Contract("_, !null -> !null")
    public String getString(@NotNull String path, @Nullable String def) {
        return config.getString(path, def);
    }

    /**
     * Gets the string at the specified path in the plugin configuration. Will return the default value if the specified path does not exist
     * or if its value is set to {@code null}. If the string does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown.
     *
     * @param path      the path to the string.
     * @param def       the default value.
     * @param condition the condition for the configuration value.
     * @return the string at the specified path, or the default value if the path is not found.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the string does not meet the specified condition.
     */
    @Contract("_, !null, _ -> !null")
    public String getStringOrDefault(@NotNull String path, @Nullable String def, @NotNull Predicate<String> condition) {
        return getStringOrDefault(path, def, condition, null);
    }

    /**
     * Gets the string at the specified path in the plugin configuration. Will return the default value if the specified path does not exist
     * or if its value is set to {@code null}. If the string does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown with the specified message.
     *
     * @param path           the path to the string.
     * @param def            the default value.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the string at the specified path, or the default value if the path is not found.
     * @throws MissingConfigValueException if there is no value set at the specified path.
     * @throws InvalidConfigValueException if the string does not meet the specified condition.
     */
    @Contract("_, !null, _, _ -> !null")
    public String getStringOrDefault(@NotNull String path, @Nullable String def, @NotNull Predicate<String> condition,
                                     @Nullable String detailsMessage) {
        final String value = getString(path, def);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Checks if the value at the specified path in the plugin configuration is a string.
     *
     * @param path the path to check.
     * @return {@code true} if the value at the specified path is a string, {@code false} otherwise.
     */
    public boolean isString(@NotNull String path) {
        return config.isString(path);
    }

    /*
     * INTEGERS
     */

    /**
     * Gets the integer at the specified path in the plugin configuration.
     *
     * @param path the path to the integer.
     * @return the integer at the specified path, or {@code 0} if the specified path does not exist, or if its value is not an integer.
     */
    public int getInt(@NotNull String path) {
        return config.getInt(path);
    }

    /**
     * Gets the integer at the specified path in the plugin configuration. If the integer does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the integer.
     * @param condition the condition for the configuration value.
     * @return the integer at the specified path, or {@code 0} if the specified path does not exist, or if its value is not an integer.
     * @throws InvalidConfigValueException if the integer does not meet the specified condition.
     */
    public int getInt(@NotNull String path, @NotNull Predicate<Integer> condition) {
        return getInt(path, condition, null);
    }

    /**
     * Gets the integer at the specified path in the plugin configuration. If the integer does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown with the specified details message.
     *
     * @param path           the path to the integer.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the integer at the specified path, or {@code 0} if the specified path does not exist, or if its value is not an integer.
     * @throws InvalidConfigValueException if the integer does not meet the specified condition.
     */
    public int getInt(@NotNull String path, @NotNull Predicate<Integer> condition, @Nullable String detailsMessage) {
        final int value = getInt(path);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Gets the integer at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not an integer.
     *
     * @param path the path to the integer.
     * @param def  the default value.
     * @return the integer at the specified path, or the default value if the specified path does not exist, or if its value is not an
     * integer.
     */
    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Gets the integer at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not an integer. If the integer does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown.
     *
     * @param path      the path to the integer.
     * @param def       the default value.
     * @param condition the condition for the configuration value.
     * @return the integer at the specified path, or the default value if the specified path does not exist, or if its value is not an
     * integer.
     * @throws InvalidConfigValueException if the integer does not meet the specified condition.
     */
    public int getIntOrDefault(@NotNull String path, int def, @NotNull Predicate<Integer> condition) {
        return getIntOrDefault(path, def, condition, null);
    }

    /**
     * Gets the integer at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not an integer. If the integer does not meet the specified condition, a {@link InvalidConfigValueException} is
     * thrown.
     *
     * @param path           the path to the integer.
     * @param def            the default value.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the integer at the specified path, or the default value if the specified path does not exist, or if its value is not an
     * integer.
     * @throws InvalidConfigValueException if the integer does not meet the specified condition.
     */
    public int getIntOrDefault(@NotNull String path, int def, @NotNull Predicate<Integer> condition, @Nullable String detailsMessage) {
        final int value = getInt(path, def);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Checks whether the value at the specified path is an integer.
     *
     * @param path the path to check for.
     * @return {@code true} if the specified path exists and is set to an integer value, {@code false} otherwise.
     */
    public boolean isInt(@NotNull String path) {
        return config.isInt(path);
    }

    /*
     * DOUBLES
     */

    /**
     * Gets the double at the specified path in the plugin configuration.
     *
     * @param path the path to the double.
     * @return the double at the specified path, or {@code 0} if the specified path does not exist, or its value is not a double.
     */
    public double getDouble(@NotNull String path) {
        return config.getDouble(path);
    }

    /**
     * Gets the double at the specified path in the plugin configuration. If the double does not meet the specified condition, a
     * {@link IllegalArgumentException} is thrown.
     *
     * @param path      the path to the double.
     * @param condition the condition for the configuration value.
     * @return the double at the specified path, or {@code 0} if the specified path does not exist, or its value is not a double.
     * @throws IllegalArgumentException if the double does not meet the specified condition.
     */
    public double getDouble(@NotNull String path, @NotNull Predicate<Double> condition) {
        return getDouble(path, condition, null);
    }

    /**
     * Gets the double at the specified path in the plugin configuration. If the double does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown with the specified message.
     *
     * @param path           the path to the double.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the double at the specified path, or {@code 0} if the specified path does not exist, or its value is not a double.
     * @throws InvalidConfigValueException if the double does not meet the specified condition.
     */
    public double getDouble(@NotNull String path, @NotNull Predicate<Double> condition, @Nullable String detailsMessage) {
        final double value = getDouble(path);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Gets the double at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not a double.
     *
     * @param path the path to the double.
     * @param def  the default value.
     * @return the double at the specified path, or the default value if the specified path does not exist, or if its value is not a double.
     */
    public double getDouble(@NotNull String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * Gets the double at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not a double. If the double does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown.
     *
     * @param path      the path to the double.
     * @param def       the default value.
     * @param condition the condition for the configuration value.
     * @return the double at the specified path, or the default value if the specified path does not exist, or if its value is not a double.
     * @throws InvalidConfigValueException if the double does not meet the specified condition.
     */
    public double getDouble(@NotNull String path, double def, @NotNull Predicate<Double> condition) {
        return getDouble(path, def, condition, null);
    }

    /**
     * Gets the double at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not a double. If the double does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown with the specified message.
     *
     * @param path           the path to the double.
     * @param def            the default value.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the double at the specified path, or the default value if the specified path does not exist, or if its value is not a double.
     * @throws InvalidConfigValueException if the double does not meet the specified condition.
     */
    public double getDouble(@NotNull String path, double def, @NotNull Predicate<Double> condition, @Nullable String detailsMessage) {
        final double value = getDouble(path, def);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /*
     * LONGS
     */

    /**
     * Gets the long at the specified path in the plugin configuration.
     *
     * @param path the path to the long.
     * @return the long at the specified path, or {@code 0} if the specified path does not exist, or its value is not a long.
     */
    public long getLong(@NotNull String path) {
        return config.getLong(path);
    }

    /**
     * Gets the long at the specified path in the plugin configuration. If the long does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the long.
     * @param condition the condition for the configuration value.
     * @return the long at the specified path, or {@code 0} if the specified path does not exist, or its value is not a long.
     * @throws InvalidConfigValueException if the long does not meet the specified condition.
     */
    public long getLong(@NotNull String path, @NotNull Predicate<Long> condition) {
        return getLong(path, condition, null);
    }

    /**
     * Gets the long at the specified path in the plugin configuration. If the long does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown with the specified message.
     *
     * @param path           the path to the long.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the long at the specified path, or {@code 0} if the specified path does not exist, or its value is not a long.
     * @throws InvalidConfigValueException if the long does not meet the specified condition.
     */
    public long getLong(@NotNull String path, @NotNull Predicate<Long> condition, @Nullable String detailsMessage) {
        final long value = config.getLong(path);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Gets the long at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not a long.
     *
     * @param path the path to the long.
     * @param def  the default value.
     * @return the long at the specified path, or the default value if the specified path does not exist, or if its value is not a long.
     */
    public long getLong(@NotNull String path, long def) {
        return config.getLong(path, def);
    }

    /**
     * Gets the long at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not a long. If the long does not meet the specified condition, an {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the long.
     * @param def       the default value.
     * @param condition the condition for the configuration value.
     * @return the long at the specified path, or the default value if the specified path does not exist, or if its value is not a long.
     * @throws InvalidConfigValueException if the long does not meet the specified condition.
     */
    public long getLong(@NotNull String path, long def, @NotNull Predicate<Long> condition) {
        return getLong(path, def, condition, null);
    }

    /**
     * Gets the long at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist or if its value is not a long. If the long does not meet the specified condition, an {@link InvalidConfigValueException} is thrown
     * with the specified message.
     *
     * @param path           the path to the long.
     * @param def            the default value.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the long at the specified path, or the default value if the specified path does not exist, or if its value is not a long.
     * @throws InvalidConfigValueException if the long does not meet the specified condition.
     */
    public long getLong(@NotNull String path, long def, @NotNull Predicate<Long> condition, @Nullable String detailsMessage) {
        final long value = getLong(path, def);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /*
     * BOOLEANS
     */

    /**
     * Gets the boolean at the specified path in the plugin configuration.
     *
     * @param path the path to the boolean.
     * @return the boolean at the specified path, or {@code false} if the specified path does not exist, or its value is not a boolean.
     */
    public boolean getBoolean(@NotNull String path) {
        return config.getBoolean(path);
    }

    /**
     * Gets the boolean at the specified path in the plugin configuration. If the boolean does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown.
     *
     * @param path      the path to the boolean.
     * @param condition the condition for the configuration value.
     * @return the boolean at the specified path, or {@code false} if the specified path does not exist, or its value is not a boolean.
     * @throws InvalidConfigValueException if the boolean does not meet the specified condition.
     */
    public boolean getBoolean(@NotNull String path, @NotNull Predicate<Boolean> condition) {
        return getBoolean(path, condition, null);
    }

    /**
     * Gets the boolean at the specified path in the plugin configuration. If the boolean does not meet the specified condition, an
     * {@link InvalidConfigValueException} is thrown with the specified message.
     *
     * @param path           the path to the boolean.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the boolean at the specified path, or {@code false} if the specified path does not exist, or its value is not a boolean.
     * @throws InvalidConfigValueException if the boolean does not meet the specified condition.
     */
    public boolean getBoolean(@NotNull String path, @NotNull Predicate<Boolean> condition, @Nullable String detailsMessage) {
        final boolean value = getBoolean(path);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Gets the boolean at the specified path in the plugin configuration. Will return the default value if the specified path does not
     *
     * @param path the path to the boolean.
     * @param def  the default value.
     * @return the boolean at the specified path, or the default value if the specified path does not exist, or its value is not a boolean.
     */
    public boolean getBoolean(@NotNull String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * Gets the boolean at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist, or its value is not a boolean. If the boolean does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown.
     *
     * @param path      the path to the boolean.
     * @param def       the default value.
     * @param condition the condition for the configuration value.
     * @return the boolean at the specified path, or the default value if the specified path does not exist, or its value is not a boolean.
     * @throws InvalidConfigValueException if the boolean does not meet the specified condition.
     */
    public boolean getBoolean(@NotNull String path, boolean def, @NotNull Predicate<Boolean> condition) {
        return getBoolean(path, def, condition, null);
    }

    /**
     * Gets the boolean at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist, or its value is not a boolean. If the boolean does not meet the specified condition, an {@link InvalidConfigValueException} is
     * thrown with the specified message.
     *
     * @param path           the path to the boolean.
     * @param def            the default value.
     * @param condition      the condition for the configuration value.
     * @param detailsMessage the details message to display if the condition is not met, can be {@code null}.
     * @return the boolean at the specified path, or the default value if the specified path does not exist, or its value is not a boolean.
     * @throws InvalidConfigValueException if the boolean does not meet the specified condition.
     */
    public boolean getBoolean(@NotNull String path, boolean def, @NotNull Predicate<Boolean> condition, @Nullable String detailsMessage) {
        final boolean value = getBoolean(path, def);
        if (!condition.test(value))
            throw new InvalidConfigValueException(path, detailsMessage);
        return value;
    }

    /**
     * Checks whether the value at the specified path is a boolean.
     *
     * @param path the path to check.
     * @return {@code true} if the value at the specified path is a boolean, {@code false} otherwise.
     */
    public boolean isBoolean(@NotNull String path) {
        return config.isBoolean(path);
    }

    /*
     * COLLECTIONS
     */

    /**
     * Gets the list at the specified path in the plugin configuration.
     *
     * @param path the path to the list.
     * @param <T>  the type of the elements of the returned list.
     * @return the list at the specified path, or an empty list if the specified path does not exist, or its value is not a list.
     */
    public @NotNull <T> List<T> getList(@NotNull String path) {
        final List<T> list;
        try {
            //noinspection unchecked
            list = new ArrayList<>((List<T>) Objects.requireNonNull(config.getList(path)));
        } catch (Exception ex) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * Gets the list at the specified path in the plugin configuration.
     *
     * @param path the path to the list.
     * @param <T>  the type of the elements of the returned list.
     * @return the list at the specified path, or an empty list if the specified path does not exist, or its value is not a list.
     */
    public @NotNull <T> Set<T> getSet(@NotNull String path) {
        final Set<T> set;
        try {
            //noinspection unchecked
            set = new HashSet<>((List<T>) Objects.requireNonNull(config.getList(path)));
        } catch (Exception ex) {
            return Collections.emptySet();
        }
        return set;
    }

    /**
     * Gets the list at the specified path in the plugin configuration.
     *
     * @param path the path to the list.
     * @return the list at the specified path, or an empty list if the specified path does not exist, or its value is not a list.
     */
    public @NotNull List<Map<?, ?>> getMapList(@NotNull String path) {
        return config.getMapList(path);
    }

    /**
     * Checks whether the value at the specified path is a list.
     *
     * @param path the path to check.
     * @return {@code true} if the value at the specified path is a list, {@code false} otherwise.
     */
    public boolean isList(@NotNull String path) {
        return config.isList(path);
    }

    /*
     * SECTIONS
     */

    /**
     * Gets the section at the specified path in the plugin configuration.
     *
     * @param path the path to the section.
     * @return the section at the specified path, or an empty map if the specified path does not exist, or its value is not a section.
     */
    public @NotNull Map<String, Object> getSection(@NotNull String path) {
        if (!config.isConfigurationSection(path))
            return Collections.emptyMap();
        return Objects.requireNonNull(config.getConfigurationSection(path)).getValues(true);
    }

    /**
     * Checks whether the value at the specified path is a section.
     *
     * @param path the path to check.
     * @return {@code true} if the value at the specified path is a section, {@code false} otherwise.
     */
    public boolean isSection(@NotNull String path) {
        return config.isConfigurationSection(path);
    }

    /**
     * Gets the section at the specified path in the plugin configuration. Will return the default value if the specified path does not
     * exist, or its value is not a section.
     *
     * @param path the path to the section.
     * @param def  the default value.
     * @return the section at the specified path, or the default value if the specified path does not exist, or its value is not a section.
     */
    @Contract("_, !null -> !null")
    public Map<String, Object> getSection(@NotNull String path, @Nullable Map<String, Object> def) {
        if (!config.isConfigurationSection(path))
            return def;
        final ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null)
            return def;
        final Map<String, Object> map = section.getValues(true);
        if (def != null)
            putDefaults(map, def);
        return map;
    }

    /**
     * Puts the default values into the specified map if the keys do not already exist.
     *
     * @param map      the map.
     * @param defaults a map containing the default values.
     */
    private <K, V> void putDefaults(@NotNull Map<String, Object> map, @NotNull Map<String, Object> defaults) {
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            if (!map.containsKey(entry.getKey()))
                map.putIfAbsent(entry.getKey(), entry.getValue());
            else {
                if (entry.getValue() instanceof MemorySection) {
                    putDefaults(((MemorySection) entry.getValue()).getValues(true),
                            ((MemorySection) map.get(entry.getKey())).getValues(true));
                }
            }
        }
    }

    /*
     * PARSING
     */

    /**
     * Parses the specified path in the plugin configuration using the specified parser.
     *
     * @param path   the path to parse.
     * @param parser the parser function.
     * @param <T>    the type of the parsed value.
     * @return the parsed value.
     */
    @NotNull
    public <T> T parse(@NotNull String path, @NotNull Function<String, T> parser) {
        return parse(path, parser, false);
    }

    /**
     * Parses the specified path in the plugin configuration using the specified parser.
     *
     * @param path     the path to parse.
     * @param parser   the parser function.
     * @param optional {@code true} whether this may return {@code null}.
     * @param <T>      the type of the parsed value.
     * @return the parsed value.
     */
    @Contract("_, _, false -> !null")
    public <T> T parse(@NotNull String path, @NotNull Function<String, T> parser, boolean optional) throws InvalidConfigValueException {
        if (!isString(path)) {
            if (optional) return null;
            else throw new MissingConfigValueException(path);
        }
        try {
            return parser.apply(Objects.requireNonNull(getString(path)));
        } catch (Exception ex) {
            if (optional) return null;
            else throw new InvalidConfigValueException("Failed to parse value at path '" + path + "' from config", ex);
        }
    }

    /**
     * Parses the specified path in the plugin configuration using the specified parser. Returns the default value if the path does not
     * exist or could not be deserialized with the provided deserializer.
     *
     * @param path   the path to parse.
     * @param parser the parser function.
     * @param def    the default value.
     * @param <T>    the type of the parsed value.
     * @return the parsed value.
     */
    @Contract("_, _, !null -> !null")
    public <T> T parse(@NotNull String path, @NotNull Function<String, T> parser, @Nullable T def) {
        if (!isString(path))
            return def;
        try {
            return parser.apply(Objects.requireNonNull(getString(path)));
        } catch (Exception ex) {
            return def;
        }
    }

    /*
     * DESERIALIZATION
     */

    /**
     * Gets a deserialized object at the specified path in the plugin configuration.
     *
     * @param path         the path to the list.
     * @param deserializer the deserializer function.
     * @param <T>          the type of the deserialized objects.
     * @return the deserialized object at the specified path.
     */
    @NotNull
    public <T> T deserialize(@NotNull String path, @NotNull Function<Map<String, Object>, T> deserializer) {
        return deserialize(path, deserializer, false);
    }

    /**
     * Gets a deserialized object at the specified path in the plugin configuration.
     *
     * @param path         the path to the list.
     * @param deserializer the deserializer function.
     * @param optional     {@code true} if this may return {@code null}.
     * @param <T>          the type of the deserialized objects.
     * @return the deserialized object at the specified path.
     */
    @Contract("_, _, false -> !null")
    public <T> T deserialize(@NotNull String path, @NotNull Function<Map<String, Object>, T> deserializer, boolean optional) {
        if (!config.isConfigurationSection(path)) {
            if (optional) return null;
            else throw new MissingConfigValueException(path);
        }
        try {
            return deserializer.apply(Objects.requireNonNull(config.getConfigurationSection(path)).getValues(false));
        } catch (Exception ex) {
            if (optional) return null;
            else throw new InvalidConfigValueException("Failed to deserialize value at path '" + path + "' from config", ex);
        }
    }

    /**
     * Gets a deserialized object at the specified path in the plugin configuration. Returns the default value if the path does not exist
     * or could not be deserialized with the provided deserializer.
     *
     * @param path         the path to the list.
     * @param deserializer the deserializer function.
     * @param def          the default value.
     * @param <T>          the type of the deserialized objects.
     * @return the deserialized object at the specified path, or the default value if the path does not exist or could not be deserialized.
     */
    @Contract("_, _, !null -> !null")
    public <T> T deserialize(@NotNull String path, @NotNull Function<Map<?, ?>, T> deserializer, @Nullable T def) {
        if (!config.isConfigurationSection(path))
            return def;
        try {
            return deserializer.apply(Objects.requireNonNull(config.getConfigurationSection(path)).getValues(false));
        } catch (Exception ex) {
            return def;
        }
    }

    /**
     * Gets a list of deserialized objects at the specified path in the plugin configuration.
     *
     * @param path         the path to the list.
     * @param deserializer the deserializer function.
     * @param <T>          the type of the deserialized objects.
     * @return a list of deserialized objects at the specified path.
     */
    @NotNull
    public <T> List<T> deserializeList(@NotNull String path, @NotNull Function<Map<?, ?>, T> deserializer) {
        if (!isList(path))
            return Collections.emptyList();
        final List<T> list = new ArrayList<>();
        for (Map<?, ?> map : getMapList(path)) {
            try {
                list.add(deserializer.apply(map));
            } catch (Exception ex) {
                throw new InvalidConfigValueException("Failed to deserialize value from list at path '" + path + "' from config", ex);
            }
        }
        return list;
    }

    /**
     * Checks if the plugin configuration contains the specified path.
     *
     * @param path the path to check.
     * @return {@code true} if the plugin configuration contains the specified path, {@code false} otherwise.
     */
    public boolean contains(@NotNull String path) {
        return config.contains(path);
    }
}
