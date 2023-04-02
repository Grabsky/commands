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
package cloud.grabsky.commands.argument;

import cloud.grabsky.commands.ArgumentQueue;
import cloud.grabsky.commands.RootCommandContext;
import cloud.grabsky.commands.component.ArgumentParser;
import cloud.grabsky.commands.component.CompletionsProvider;
import cloud.grabsky.commands.exception.ArgumentParseException;
import cloud.grabsky.commands.exception.IncompatibleSenderException;
import cloud.grabsky.commands.exception.MissingInputException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Converts {@link String} literal to {@link OfflinePlayer}.
 */
public enum OfflinePlayerArgument implements CompletionsProvider, ArgumentParser<OfflinePlayer> {
    /* SINGLETON */ INSTANCE;

    @Override
    public @NotNull List<String> provide(final @NotNull RootCommandContext context) {
        return (context.getExecutor().isPlayer() == true)
                ? Stream.concat(Bukkit.getOnlinePlayers().stream()
                        .filter((player) -> context.getExecutor().asPlayer().canSee(player) == true) // Making sure not to show hidden players...
                        .map(Player::getName), Stream.of("@self")).toList()
                : Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .toList();
    }

    @Override
    public @NotNull OfflinePlayer parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws ArgumentParseException, MissingInputException, IncompatibleSenderException {
        final String value = arguments.nextString();
        // ...
        final OfflinePlayer offlinePlayer = switch (value.toLowerCase()) {
            case "@self" -> context.getExecutor().asPlayer();
            default -> {
                try {
                    yield (Bukkit.getPlayerExact(value) != null)
                            ? Bukkit.getPlayerExact(value)
                            : (Bukkit.getOfflinePlayerIfCached(value) != null)
                                    ? Bukkit.getOfflinePlayerIfCached(value)
                                    : Bukkit.getOfflinePlayer(UUID.fromString(value));
                } catch (final IllegalArgumentException e) {
                    throw new OfflinePlayerArgument.Exception(value, e);
                }
            }
        };
        // ...
        if (offlinePlayer != null)
            return offlinePlayer;
        // ...
        throw new OfflinePlayerArgument.Exception(value);
    }

    /**
     * {@link Exception} is thrown when invalid name or uuid is provided for {@link OfflinePlayer} argument type.
     */
    public static final class Exception extends ArgumentParseException {

        private Exception(final String inputValue) {
            super(inputValue);
        }

        private Exception(final String inputValue, final Throwable cause) {
            super(inputValue, cause);
        }

    }

}