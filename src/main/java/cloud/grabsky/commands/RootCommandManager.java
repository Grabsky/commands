/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * HORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cloud.grabsky.commands;

import cloud.grabsky.commands.arguments.*;
import cloud.grabsky.commands.components.ArgumentParser;
import cloud.grabsky.commands.components.CompletionsProvider;
import cloud.grabsky.commands.components.ExceptionHandler;
import cloud.grabsky.commands.exception.CommandLogicException;
import cloud.grabsky.commands.exception.IncompatibleParserException;
import cloud.grabsky.commands.util.Arrays;
import io.papermc.paper.math.Position;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

// TO-DO: Finish JavaDocs and GitHub documentation.
// TO-DO: ArgumentQueue#peek
// TO-DO: More testing and improvements based on Q/A results.
public final class RootCommandManager {

    @Getter(AccessLevel.PUBLIC)
    private final Plugin plugin;

    private final Set<RootCommand> commands;
    private final Map<Class<?>, ArgumentParser<?>> argumentParsers;
    private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlers;
    private final Map<Class<?>, CompletionsProvider> completionsProviders;

    private final RootCommandManager that;

    public RootCommandManager(final Plugin plugin) {
        this.plugin = plugin;
        this.commands = new HashSet<>();
        this.argumentParsers = new HashMap<>();
        this.exceptionHandlers = new HashMap<>();
        this.completionsProviders = new HashMap<>();
        this.that = this;
        // java.lang.String
        this.setArgumentParser(String.class, StringArgument.INSTANCE);
        // java.lang.Integer
        this.setArgumentParser(Integer.class, IntegerArgument.INSTANCE);
        // java.lang.Float
        this.setArgumentParser(Float.class, FloatArgument.INSTANCE);
        // java.lang.Double
        this.setArgumentParser(Double.class, DoubleArgument.INSTANCE);
        // java.lang.Boolean
        this.setArgumentParser(Boolean.class, BooleanArgument.INSTANCE);
        // net.kyori.adventure.text.Component
        this.setArgumentParser(Component.class, ComponentArgument.INSTANCE);
        // org.bukkit.entity.Player
        this.setArgumentParser(Player.class, PlayerArgument.INSTANCE);
        this.setCompletionsProvider(Player.class, PlayerArgument.INSTANCE);
        // org.bukkit.Material
        this.setArgumentParser(Material.class, MaterialArgument.INSTANCE);
        this.setCompletionsProvider(Material.class, MaterialArgument.INSTANCE);
        // org.bukkit.enchantments.Enchantment
        this.setArgumentParser(Enchantment.class, EnchantmentArgument.INSTANCE);
        this.setCompletionsProvider(Enchantment.class, EnchantmentArgument.INSTANCE);
        // org.bukkit.entity.EntityType
        this.setArgumentParser(EntityType.class, EntityTypeArgument.INSTANCE);
        this.setCompletionsProvider(EntityType.class, EntityTypeArgument.INSTANCE);
        // io.papermc.paper.math.Position
        this.setArgumentParser(Position.class, PositionArgument.INSTANCE);
        this.setCompletionsProvider(Position.class, PositionArgument.INSTANCE);
    }

    /* COMMANDS */

    /**
     * Registers command from provided {@link RootCommand} instance.
     */
    public void registerCommand(final RootCommand sCommand) {

        final Command bCommand = new Command(sCommand.getName()) {

            @Override @SuppressWarnings({"unchecked", "rawtypes"})
            public boolean execute(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args) {
                final RootCommandContext context = new RootCommandContext(that, sCommand, new RootCommandExecutor(sender), label, toStringInput(label, args));
                final ArgumentQueue queue = new ArgumentQueue(context, Arrays.toArrayList(args));
                // handling command logic (and exceptions)
                try {
                    sCommand.onCommand(context, queue);
                    return true;
                } catch (final CommandLogicException exc) {
                    final Class<? extends CommandLogicException> exceptionClass = exc.getClass();
                    // handling exceptions using default handlers
                    if (exc.isHandlerFinal() == false && getExceptionHandler(exceptionClass) != null) {
                        ((ExceptionHandler) getExceptionHandler(exceptionClass)).handle(exceptionClass.cast(exc), context); // should not be null; raw cast required for compilation
                        return false;
                    }
                    // handling "non-final" exceptions (with handlers registered to ExceptionHandlerRegistry)
                    exc.accept(context);
                    return false;
                } catch (final Throwable e) {
                    sender.sendMessage("An unexpected error occurred while executing the command.");
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String alias, @NotNull final String[] args) throws IllegalArgumentException {
                // disabling completions for invalid input
                if (args.length > 1 && args[args.length - 2].isEmpty() == true) {
                    return Arrays.EMPTY_STRING_LIST;
                }
                // handling
                final RootCommandContext context = new RootCommandContext(that, sCommand, new RootCommandExecutor(sender), alias, toStringInput(alias, args));
                // returning filtered list
                try {
                    return toFilteredList(sCommand.onTabComplete(context, args.length - 1).provide(context), args[args.length - 1]);
                } catch (final CommandLogicException exc) {
                    return Arrays.EMPTY_STRING_LIST;
                }
            }

        };

        if (sCommand.getAliases() != null && sCommand.getAliases().length > 0)
            bCommand.setAliases(Arrays.toArrayList(sCommand.getAliases()));

        if (sCommand.getPermission() != null)
            bCommand.setPermission(sCommand.getPermission());

        if (sCommand.getUsage() != null)
            bCommand.setUsage(sCommand.getUsage());

        if (sCommand.getDescription() != null) {
            bCommand.setDescription(sCommand.getDescription());
        }

        // registering command to the server
        plugin.getServer().getCommandMap().register(sCommand.getName(), bCommand);

        // adding command to the set
        commands.add(sCommand);
    }

    /**
     * Registers command from provided {@link Class}.
     * Class must be either an enum or have an empty constructor.
     *
     * @throws IllegalArgumentException if class is inaccessible or cannot be initialized.
     */
    public <T extends RootCommand> void registerCommand(final Class<T> commandClass) throws IllegalArgumentException {
        try {
            final RootCommand commandObject = (commandClass.isEnum() == true)
                    ? commandClass.getEnumConstants()[0]
                    : commandClass.getConstructor().newInstance();
            // registering...
            this.registerCommand(commandObject);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exc) {
            throw new IllegalArgumentException(commandClass.getSimpleName() + " must be either an enum or have an empty constructor.");
        }
    }

    /**
     * Returns an unmodifiable copy of {@link HashSet} containing all commands registered by this manager.
     */
    public Set<RootCommand> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    /* ARGUMENT PARSER */

    /**
     * Returns {@link ArgumentParser ArgumentParser&lt;T&gt;} for specified type.
     **
     * @apiNote This is internal API that can change at any time.
     */
    @Internal
    @SuppressWarnings("unchecked")
    public <T> ArgumentParser<T> getArgumentParser(final Class<T> type) throws IncompatibleParserException {
        if (argumentParsers.containsKey(type) == true)
            return (ArgumentParser<T>) argumentParsers.get(type);
        // ...
        throw new IncompatibleParserException(type);
    }

    /**
     * Sets {@link ArgumentParser ArgumentParser&lt;T&gt;} (parser) as a default parser {@link T} (type).
     * If a parser for this {@link T} (type) already exists - it gets overridden.
     */
     public <T> void setArgumentParser(final Class<T> type, final ArgumentParser<T> parser) {
        argumentParsers.put(type, parser);
    }

    /* EXCEPTION HANDLER */

    /**
     * Returns {@link ExceptionHandler ExceptionHandler&lt;E&gt;} for specified type, or default.
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal
    @SuppressWarnings("unchecked")
    public <E extends CommandLogicException> @Nullable ExceptionHandler<E> getExceptionHandler(final Class<E> type) {
        return (ExceptionHandler<E>) exceptionHandlers.get(type);
    }

    /**
     * Sets {@link ExceptionHandler ExceptionHandler&lt;E&gt;} (handler) as a default exception handler {@link E} (type).
     * If a handler for this {@link E} (type) already exists - it gets overridden.
     */
    public <E extends CommandLogicException> void setExceptionHandler(final Class<E> type, final ExceptionHandler<E> handler) {
        exceptionHandlers.put(type, handler);
    }

    /* COMPLETIONS PROVIDER */

    /**
     * Returns {@link CompletionsProvider} for specified type, or {@link CompletionsProvider#EMPTY}.
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal
    public <T> CompletionsProvider getCompletionsProvider(final Class<T> type) {
        return (completionsProviders.containsKey(type) == true) ? completionsProviders.get(type) : CompletionsProvider.EMPTY;
    }

    /**
     * Sets {@link CompletionsProvider CompletionsProvider} (provider) as a default completions provider {@link T} (type).
     * If a provider for this {@link T} (type) already exists - it gets overridden.
     */
    public <T> void setCompletionsProvider(final Class<T> type, final CompletionsProvider provider) {
        completionsProviders.put(type, provider);
    }

    /* STATIC HELPERS */

    private static String toStringInput(final String label, final String[] arguments) {
        final StringBuilder input = new StringBuilder(label);
        // ...
        for (final String argument : arguments)
            if (argument != null && argument.isEmpty() == false)
                input.append(" ").append(argument);
        // ...
        return input.toString();
    }

    private static List<String> toFilteredList(final List<String> list, final String input) {
        return list.stream()
                .filter(element -> element.toLowerCase().contains(input.toLowerCase()) == true)
                .toList();
    }

}
