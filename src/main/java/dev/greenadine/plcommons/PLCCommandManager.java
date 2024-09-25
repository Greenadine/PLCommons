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
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Split;
import co.aikar.commands.annotation.Values;
import co.aikar.commands.contexts.ContextResolver;
import co.aikar.commands.contexts.OptionalContextResolver;
import dev.greenadine.plcommons.exception.PLCInvalidCommandArgument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * A command manager that allows for easy command registration and management.
 *
 * @since 0.1
 */
public class PLCCommandManager {

    private final HashMap<RegisteredCommand<?>, RegisteredCommandHelp> commandHelpMap = new HashMap<>();

    private final BukkitCommandManager commandManager;
    private final PLCLanguageManager languageManager;

    public PLCCommandManager(@NotNull PLCLanguageManager languageManager) {
        this.commandManager = ServerPlatform.newCommandManager();
        this.languageManager = languageManager;
        registerContextResolvers();
        PLCommons.getDependencyManager().useCommandManagerDependencies((ExtendedCommandManager) commandManager);  // Make the dependency manager use the dependencies table of this command manager
    }

    /**
     * Gets the command manager.
     *
     * @return the command manager.
     */
    public @NotNull BukkitCommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the language manager.
     *
     * @return the language manager.
     */
    public @NotNull PLCLanguageManager getLanguageManager() {
        return languageManager;
    }

    /**
     * Registers a command with the command manager.
     *
     * @param command the command to register.
     */
    public final void registerCommand(@NotNull PLCommonsCommand command) {
        commandManager.registerCommand(command);
        command.onRegister(languageManager);
    }

    /**
     * Overrides default context resolvers to better fit PLCommons.
     */
    private void registerContextResolvers() {
        // Override default context resolvers
        // Primitives
        registerContext(Long.class, Long.TYPE, c -> resolveNumber(c, Long.MIN_VALUE, Long.MAX_VALUE).longValue());
        registerContext(Integer.class, Integer.TYPE, c -> resolveNumber(c, Integer.MIN_VALUE, Integer.MAX_VALUE).intValue());
        registerContext(Short.class, Short.TYPE, c -> resolveNumber(c, Short.MIN_VALUE, Short.MAX_VALUE).shortValue());
        registerContext(Byte.class, Byte.TYPE, c -> resolveNumber(c, Byte.MIN_VALUE, Byte.MAX_VALUE).byteValue());
        registerContext(Double.class, Double.TYPE, c -> resolveNumber(c, Double.MIN_VALUE, Double.MAX_VALUE).doubleValue());
        registerContext(Float.class, Float.TYPE, c -> resolveNumber(c, Float.MIN_VALUE, Float.MAX_VALUE).floatValue());
        registerContext(Boolean.class, Boolean.TYPE, c -> ACFUtil.isTruthy(c.popFirstArg()));
        registerContext(Character.class, Character.TYPE, c -> {
            String arg = c.popFirstArg();
            if (arg.length() > 1) {
                throw new PLCInvalidCommandArgument(MessageKeys.MUST_BE_MAX_LENGTH, "{max}", String.valueOf(1));
            }
            return arg.charAt(0);
        });

        // Get the command contexts
        final CommandContexts<BukkitCommandExecutionContext> contexts = commandManager.getCommandContexts();

        // Numbers
        contexts.registerContext(Number.class, c -> resolveNumber(c, Double.MIN_VALUE, Double.MAX_VALUE));
        contexts.registerContext(BigDecimal.class, this::resolveBigNumber);
        contexts.registerContext(BigInteger.class, this::resolveBigNumber);

        // Strings
        contexts.registerContext(String.class, c -> {
            if (c.hasAnnotation(Values.class)) {
                return c.popFirstArg();
            }

            String val = c.isLastArg() && !c.hasAnnotation(Single.class) ? ACFUtil.join(c.getArgs()) : c.popFirstArg();
            Integer minLen = c.getFlagValue("minlen", (Integer) null);
            Integer maxLen = c.getFlagValue("maxlen", (Integer) null);
            if (minLen != null && val.length() < minLen) {
                throw new PLCInvalidCommandArgument(MessageKeys.MUST_BE_MIN_LENGTH, "{min}", String.valueOf(minLen));
            } else if (maxLen != null && val.length() > maxLen) {
                throw new PLCInvalidCommandArgument(MessageKeys.MUST_BE_MAX_LENGTH, "{max}", String.valueOf(minLen));
            } else {
                return val;
            }
        });
        contexts.registerContext(String[].class, c -> {
            List<String> args = c.getArgs();
            String val;
            if (c.isLastArg() && !c.hasAnnotation(Single.class)) {
                val = ACFUtil.join(args);
            } else {
                val = c.popFirstArg();
            }

            String split = c.getAnnotationValue(Split.class, 8);
            if (split != null) {
                if (val.isEmpty()) {
                    throw new PLCInvalidCommandArgument();
                } else {
                    return PLCPatterns.getPattern(split).split(val);
                }
            } else {
                if (!c.isLastArg()) {
                    ACFUtil.sneaky(new IllegalStateException("Weird Command signature... String[] should be last or @Split"));
                }

                String[] result = args.toArray(new String[0]);
                args.clear();
                return result;
            }
        });

        // Enums
        contexts.registerContext(Enum.class, (c) -> {
            String first = c.popFirstArg();
            //noinspection unchecked,deprecation
            Class<? extends Enum<?>> enumCls = (Class<? extends Enum<?>>) c.getParam().getType();
            Enum<?> match = ACFUtil.simpleMatch(enumCls, first);
            if (match == null) {
                List<String> names = ACFUtil.enumNames(enumCls);
                throw new PLCInvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_ONE_OF, "{valid}", ACFUtil.join(names, ", "));
            } else {
                return match;
            }
        });

        // Register PLC context resolvers
        contexts.registerIssuerOnlyContext(PLCCommandIssuer.class, c -> {
            final CommandSender sender = c.getSender();
            return new PLCCommandIssuer(sender, languageManager);
        });
        contexts.registerIssuerOnlyContext(PLCCommandHelp.class, c -> {
            final RegisteredCommand<?> cmd = c.getCmd();
            final RegisteredCommandHelp internalHelp = commandHelpMap.computeIfAbsent(cmd, k -> {
                final CommandOperationContext<?> commandContext = CommandManager.getCurrentCommandOperationContext();
                final String commandLabel = commandContext.getCommandLabel();
                final ExtendedBaseCommand baseCommand = (ExtendedBaseCommand) commandContext.getCommand();
                return new RegisteredCommandHelp(commandLabel, baseCommand.getSubcommands().entries(), languageManager);
            });
            return new PLCCommandHelp(internalHelp, c.getSender(), languageManager);
        });
    }

    /* Utility methods */

    /**
     * Registers a context resolver for two classes.
     *
     * @param clazz1   the first class.
     * @param clazz2   the second class.
     * @param resolver the context resolver.
     * @param <T>      the type of the context resolver.
     */
    @SuppressWarnings("unchecked")
    public <T> void registerContext(Class<? extends T> clazz1, Class<? extends T> clazz2, ContextResolver<? extends T, BukkitCommandExecutionContext> resolver) {
        // Get the command contexts
        final CommandContexts<BukkitCommandExecutionContext> contexts = commandManager.getCommandContexts();

        contexts.registerContext((Class<T>) clazz1, (ContextResolver<T, BukkitCommandExecutionContext>) resolver);
        contexts.registerContext((Class<T>) clazz2, (ContextResolver<T, BukkitCommandExecutionContext>) resolver);
    }

    /**
     * Registers an optional context resolver for two classes.
     *
     * @param clazz1   the first class.
     * @param clazz2   the second class.
     * @param supplier the optional context resolver.
     * @param <T>      the type of the optional context resolver.
     */
    public <T> void registerOptionalContext(Class<T> clazz1, Class<T> clazz2, OptionalContextResolver<T, BukkitCommandExecutionContext> supplier) {
        // Get the command contexts
        final CommandContexts<BukkitCommandExecutionContext> contexts = commandManager.getCommandContexts();

        contexts.registerOptionalContext(clazz1, supplier);
        contexts.registerOptionalContext(clazz2, supplier);
    }

    /* Number resolving */

    /**
     * Resolves a big number.
     *
     * @param c   the command context.
     * @param <T> the type of the number.
     * @return the resolved big number.
     */
    private @NotNull <T extends Number> T resolveBigNumber(@NotNull BukkitCommandExecutionContext c) {
        String arg = c.popFirstArg();
        try {
            //noinspection unchecked
            T number = (T) ACFUtil.parseBigNumber(arg, c.hasFlag("suffixes"));
            this.validateMinMax(c, number);
            return number;
        } catch (NumberFormatException ex) {
            throw new PLCInvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, "{num}", arg);
        }
    }

    /**
     * Resolves a number.
     *
     * @param c        the command context.
     * @param minValue the minimum value.
     * @param maxValue the maximum value.
     * @return the resolved number.
     */
    private @NotNull Number resolveNumber(@NotNull BukkitCommandExecutionContext c, @NotNull Number minValue, @NotNull Number maxValue) {
        String number = c.popFirstArg();
        try {
            return this.parseAndValidateNumber(number, c, minValue, maxValue).shortValue();
        } catch (NumberFormatException ex) {
            throw new PLCInvalidCommandArgument(MessageKeys.MUST_BE_A_NUMBER, "{num}", number);
        }
    }

    /**
     * Parses and validates a number.
     *
     * @param number   the number to parse.
     * @param c        the command context.
     * @param minValue the minimum value.
     * @param maxValue the maximum value.
     * @return the parsed and validated number.
     * @throws PLCInvalidCommandArgument if the number is not within the specified range.
     */
    private @NotNull Number parseAndValidateNumber(String number, BukkitCommandExecutionContext c, Number minValue, Number maxValue) throws PLCInvalidCommandArgument {
        Number val = ACFUtil.parseNumber(number, c.hasFlag("suffixes"));
        this.validateMinMax(c, val, minValue, maxValue);
        return val;
    }

    /**
     * Validates the minimum and maximum values of a number.
     *
     * @param c   the command context.
     * @param val the value to validate.
     * @throws PLCInvalidCommandArgument if the value is not within the specified range.
     */
    private void validateMinMax(BukkitCommandExecutionContext c, Number val) throws PLCInvalidCommandArgument {
        this.validateMinMax(c, val, null, null);
    }

    /**
     * Validates the minimum and maximum values of a number.
     *
     * @param c        the command context.
     * @param val      the value to validate.
     * @param minValue the minimum value.
     * @param maxValue the maximum value.
     * @throws PLCInvalidCommandArgument if the value is not within the specified range.
     */
    private void validateMinMax(BukkitCommandExecutionContext c, Number val, Number minValue, Number maxValue) throws PLCInvalidCommandArgument {
        minValue = c.getFlagValue("min", minValue);
        maxValue = c.getFlagValue("max", maxValue);
        if (maxValue != null && val.doubleValue() > maxValue.doubleValue())
            throw new PLCInvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_AT_MOST, "{max}", String.valueOf(maxValue));
        else if (minValue != null && val.doubleValue() < minValue.doubleValue())
            throw new PLCInvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_AT_LEAST, "{min}", String.valueOf(minValue));
    }
}
