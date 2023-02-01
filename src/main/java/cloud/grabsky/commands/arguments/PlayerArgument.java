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
package cloud.grabsky.commands.arguments;

import cloud.grabsky.commands.components.CompletionsProvider;
import cloud.grabsky.commands.ArgumentQueue;
import cloud.grabsky.commands.SimpleCommandContext;
import cloud.grabsky.commands.components.ArgumentParser;
import cloud.grabsky.commands.exception.ArgumentParseException;
import cloud.grabsky.commands.exception.IncompatibleSenderException;
import cloud.grabsky.commands.exception.MissingInputException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Stream;

public enum PlayerArgument implements CompletionsProvider, ArgumentParser<Player> {
    /* SINGLETON */ INSTANCE;

    @Override
    public List<String> provide(final SimpleCommandContext context) {
        return (context.getExecutor().raw() instanceof Player sender)
                ? Stream.concat(Bukkit.getOnlinePlayers().stream()
                        .filter(sender::canSee) // vanish support
                        .map(Player::getName), Stream.of("@s")).toList()
                : Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .toList();
    }

    @Override
    public Player parse(final SimpleCommandContext context, final ArgumentQueue queue) throws ArgumentParseException, MissingInputException, IncompatibleSenderException {
        final String value = queue.next();
        // ...
        final Player player = (value.equalsIgnoreCase("@s") && context.getExecutor().raw() instanceof Player sender)
                ? sender
                : Bukkit.getPlayer(value);
        // ...
        if (player != null)
            return player;
        // ...
        throw new PlayerParseException(value);
    }

    /**
     * {@link PlayerParseException} is thrown when invalid player name is provided for {@link Player} argument type.
     */
    public static final class PlayerParseException extends ArgumentParseException {

        public PlayerParseException(final String inputValue) {
            super(inputValue);
        }

        public PlayerParseException(final String inputValue, final Throwable cause) {
            super(inputValue, cause);
        }

    }

}