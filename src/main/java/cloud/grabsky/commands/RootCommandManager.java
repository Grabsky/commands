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

import cloud.grabsky.commands.argument.BooleanArgument;
import cloud.grabsky.commands.argument.ComponentArgument;
import cloud.grabsky.commands.argument.DoubleArgument;
import cloud.grabsky.commands.argument.EnchantmentArgument;
import cloud.grabsky.commands.argument.EntityTypeArgument;
import cloud.grabsky.commands.argument.FloatArgument;
import cloud.grabsky.commands.argument.IntegerArgument;
import cloud.grabsky.commands.argument.LongArgument;
import cloud.grabsky.commands.argument.MaterialArgument;
import cloud.grabsky.commands.argument.NamespacedKeyArgument;
import cloud.grabsky.commands.argument.OfflinePlayerArgument;
import cloud.grabsky.commands.argument.PlayerArgument;
import cloud.grabsky.commands.argument.PositionArgument;
import cloud.grabsky.commands.argument.ShortArgument;
import cloud.grabsky.commands.argument.StringArgument;
import cloud.grabsky.commands.argument.UUIDArgument;
import cloud.grabsky.commands.argument.WorldArgument;
import cloud.grabsky.commands.component.ArgumentParser;
import cloud.grabsky.commands.component.CompletionsProvider;
import cloud.grabsky.commands.component.ExceptionHandler;
import cloud.grabsky.commands.exception.CommandLogicException;
import cloud.grabsky.commands.exception.IncompatibleParserException;
import cloud.grabsky.commands.util.Arrays;
import io.papermc.paper.math.Position;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static cloud.grabsky.commands.util.Arrays.toArrayList;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

/**
 * {@link RootCommandManager} represents a command manager that can be
 * used to register commands or customize their handling process.
 */
// TO-DO: Finish JavaDocs and GitHub documentation.
// TO-DO: ArgumentQueue#peek (?)
// TO-DO: More testing and improvements based on Q/A results.
public final class RootCommandManager {

    @Getter(AccessLevel.PUBLIC)
    private final Plugin plugin;

    private final Set<RootCommand> commands;
    private final Map<Class<?>, ArgumentParser<?>> argumentParsers;
    private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlers;
    private final Map<Class<?>, CompletionsProvider> completionsProviders;

    private static final Component UNEXPECTED_ERROR = text("An unexpected error occurred while executing the command.", RED);

    public RootCommandManager(final Plugin plugin) {
        this.plugin = plugin;
        this.commands = new HashSet<>();
        this.argumentParsers = new HashMap<>();
        this.exceptionHandlers = new HashMap<>();
        this.completionsProviders = new HashMap<>();
        // java.lang.String
        this.setArgumentParser(String.class, StringArgument.LITERAL);
        // java.lang.Short
        this.setArgumentParser(Short.class, ShortArgument.DEFAULT_RANGE);
        // java.lang.Integer
        this.setArgumentParser(Integer.class, IntegerArgument.DEFAULT_RANGE);
        // java.lang.Long
        this.setArgumentParser(Long.class, LongArgument.DEFAULT_RANGE);
        // java.lang.Float
        this.setArgumentParser(Float.class, FloatArgument.DEFAULT_RANGE);
        // java.lang.Double
        this.setArgumentParser(Double.class, DoubleArgument.DEFAULT_RANGE);
        // java.lang.Boolean
        this.setArgumentParser(Boolean.class, BooleanArgument.INSTANCE);
        this.setCompletionsProvider(Boolean.class, BooleanArgument.INSTANCE);
        // java.util.UUID
        this.setArgumentParser(UUID.class, UUIDArgument.INSTANCE);
        // net.kyori.adventure.text.Component
        this.setArgumentParser(Component.class, ComponentArgument.LITERAL);
        this.setArgumentParser(Player.class, PlayerArgument.INSTANCE);
        // org.bukkit.entity.Player
        this.setCompletionsProvider(Player.class, PlayerArgument.INSTANCE);
        // org.bukkit.OfflinePlayer
        this.setArgumentParser(OfflinePlayer.class, OfflinePlayerArgument.INSTANCE);
        this.setCompletionsProvider(OfflinePlayer.class, OfflinePlayerArgument.INSTANCE);
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
        // org.bukkit.NamespacedKey
        this.setArgumentParser(NamespacedKey.class, NamespacedKeyArgument.INSTANCE);
        // org.bukkit.World
        this.setArgumentParser(World.class, WorldArgument.INSTANCE);
        this.setCompletionsProvider(World.class, WorldArgument.INSTANCE);
    }

    /* COMMAND REGISTRATION */

    /**
     * Registers command from specified {@link RootCommand} instance.
     */
    public RootCommandManager registerCommand(final @NotNull RootCommand rCommand) {
        final RootCommandManager that = this; // Can also be resolved using 'RootCommandManager.this' but this is shorter and looks cleaner

        final Command bCommand = new Command(rCommand.getName()) {

            @Override @SuppressWarnings({"unchecked", "rawtypes"})
            public boolean execute(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String[] args) {
                final RootCommandContext context = new RootCommandContext(that, rCommand, new RootCommandExecutor(sender), new RootCommandInput(label, args));
                final ArgumentQueue queue = new ArgumentQueue(context, toArrayList(args));
                // Handling the command... and exceptions it throws
                try {
                    rCommand.onCommand(context, queue);
                    return true;
                } catch (final CommandLogicException e) {
                    final Class<? extends CommandLogicException> exceptionClass = e.getClass();
                    // Handling exceptions using ExceptionHandler<T> registry
                    if (e.isHandlerFinal() == false && that.getExceptionHandler(exceptionClass) != null) {
                        ((ExceptionHandler) that.getExceptionHandler(exceptionClass)).handle(exceptionClass.cast(e), context); // should not be null; raw cast required for compilation
                        return false;
                    }
                    // Handling exceptions using their Consumer<RootCommandContext>
                    e.accept(context);
                    return false;
                } catch (final Throwable e) { // Handling any other exceptions...
                    e.printStackTrace();
                    sender.sendMessage(UNEXPECTED_ERROR);
                    return false;
                }
            }

            @Override
            public @NotNull List<String> tabComplete(final @NotNull CommandSender sender, final @NotNull String alias, final String @NotNull [] args) throws IllegalArgumentException {
                // Disabling completions for invalid input
                if (args.length > 1 && args[args.length - 2].isEmpty() == true) {
                    return Arrays.EMPTY_STRING_LIST;
                }
                // Handling...
                final RootCommandContext context = new RootCommandContext(RootCommandManager.this, rCommand, new RootCommandExecutor(sender), new RootCommandInput(alias, args));
                try {
                    return toFilteredList(rCommand.onTabComplete(context, args.length - 1).provide(context), args[args.length - 1]);
                } catch (final CommandLogicException exc) {
                    return Arrays.EMPTY_STRING_LIST;
                }
            }

        };
        // Setting Bukkit aliases...
        if (rCommand.getAliases() != null && rCommand.getAliases().length > 0)
            bCommand.setAliases(toArrayList(rCommand.getAliases()));
        // Setting Bukkit permission...
        if (rCommand.getPermission() != null)
            bCommand.setPermission(rCommand.getPermission());
        // Setting Bukkit usage...
        if (rCommand.getUsage() != null)
            bCommand.setUsage(rCommand.getUsage());
        // Setting Bukkit description...
        if (rCommand.getDescription() != null) {
            bCommand.setDescription(rCommand.getDescription());
        }
        // Registering org.bukkit.Command to the server...
        plugin.getServer().getCommandMap().register(plugin.getName(), bCommand);
        // Adding RootCommand to the command Set...
        commands.add(rCommand);
        // ...
        return this;
    }

    /**
     * Registers command from specified {@link Class Class&lt;T&gt;}. Class must have an empty constructor.
     *
     * @throws IllegalArgumentException class is inaccessible or cannot be initialized.
     */
    public <T extends RootCommand> RootCommandManager registerCommand(final @NotNull Class<T> commandClass) throws IllegalArgumentException {
        try {
            final RootCommand commandObject = commandClass.getConstructor().newInstance();
            // Registering using this#registerCommand(RootCommand)...
            this.registerCommand(commandObject);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exc) {
            throw new IllegalArgumentException("Could not register command from " + commandClass.getName() + " class.", exc);
        }
        // ...
        return this;
    }

    /**
     * Returns an unmodifiable copy of {@link HashSet HashSet&lt;RootCommand&gt;} containing all commands registered by this manager.
     */
    public Set<RootCommand> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    /* ARGUMENT PARSER */

    /**
     * Returns {@link ArgumentParser ArgumentParser&lt;T&gt;} for specified {@link Class Class&lt;T&gt;} (type).
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal @SuppressWarnings("unchecked")
    public <T> ArgumentParser<T> getArgumentParser(final @NotNull Class<T> type) throws IncompatibleParserException {
        if (argumentParsers.containsKey(type) == true)
            return (ArgumentParser<T>) argumentParsers.get(type);
        // ...
        throw new IncompatibleParserException(type);
    }

    /**
     * Sets {@link ArgumentParser ArgumentParser&lt;T&gt;} (parser) as a default argument parser for {@link T} (type).
     */
     public <T> RootCommandManager setArgumentParser(final @NotNull Class<T> type, final @Nullable ArgumentParser<T> parser) {
         if (parser == null && argumentParsers.containsKey(type) == true)
             argumentParsers.remove(type);
         // ...
         argumentParsers.put(type, parser);
         // ...
         return this;
    }

    /* EXCEPTION HANDLER */

    /**
     * Returns {@link ExceptionHandler ExceptionHandler&lt;E&gt;} for specified {@link E} (type).
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal @SuppressWarnings("unchecked")
    public <E extends CommandLogicException> @Nullable ExceptionHandler<E> getExceptionHandler(final @NotNull Class<E> type) {
        return (ExceptionHandler<E>) exceptionHandlers.get(type);
    }

    /**
     * Sets {@link ExceptionHandler ExceptionHandler&lt;E&gt;} (handler) as a default exception handler for {@link E} (type).
     */
    public <E extends CommandLogicException> RootCommandManager setExceptionHandler(final @NotNull Class<E> type, final @Nullable ExceptionHandler<E> handler) {
        exceptionHandlers.put(type, handler);
        // ...
        return this;
    }

    /* COMPLETIONS PROVIDER */

    /**
     * Returns {@link CompletionsProvider} for specified {@link Class Class&lt;T&gt;} (type) or {@link CompletionsProvider#EMPTY} if not found.
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal
    public <T> CompletionsProvider getCompletionsProvider(final @NotNull Class<T> type) {
        return (completionsProviders.containsKey(type) == true) ? completionsProviders.get(type) : CompletionsProvider.EMPTY;
    }

    /**
     * Sets {@link CompletionsProvider CompletionsProvider} (provider) as a default completions provider for {@link T} (type).
     */
    public <T> RootCommandManager setCompletionsProvider(final @NotNull Class<T> type, final @Nullable CompletionsProvider provider) {
        if (provider == null && completionsProviders.containsKey(type) == true)
            completionsProviders.remove(type);
        // ...
        completionsProviders.put(type, provider);
        // ...
        return this;
    }

    /**
     * Applies {@link Consumer<RootCommandManager> Consumer&lt;RootCommandManager&gt;} (template) on this {@link RootCommandManager}.
     */
    public RootCommandManager apply(final @NotNull Consumer<RootCommandManager> template) {
        template.accept(this);
        // ...
        return this;
    }

    /* STATIC HELPERS */

    // Filters list to only contain elements matching 'input' argument. Case insensitive.
    private static List<String> toFilteredList(final @NotNull List<String> list, final @NotNull String input) {
        return list.stream()
                .filter(element -> element.toLowerCase().contains(input.toLowerCase()) == true)
                .toList();
    }

}
