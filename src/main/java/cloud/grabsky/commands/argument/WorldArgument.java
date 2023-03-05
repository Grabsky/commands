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
import cloud.grabsky.commands.exception.MissingInputException;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.List;

/**
 * Converts literal to {@link World}. World must be loaded.
 */
public enum WorldArgument implements CompletionsProvider, ArgumentParser<World> {
    /* SINGLETON */ INSTANCE;

    @Override
    public List<String> provide(final RootCommandContext context) {
        return Bukkit.getWorlds().stream().map((world) -> world.key().asString()).toList();
    }

    @Override
    public World parse(final RootCommandContext context, final ArgumentQueue arguments) throws ArgumentParseException, MissingInputException {
        final String value = arguments.next(String.class).asRequired();
        // ...
        final NamespacedKey key = NamespacedKey.fromString(value);
        // ...
        for (final World world : Bukkit.getWorlds()) {
            if (world.key().equals(key) == true)
                return world;
        }
        throw new WorldParseException(value);
    }

    /**
     * {@link WorldParseException WorldParseException} is thrown when invalid key is provided for {@link World} argument type.
     */
    public static final class WorldParseException extends ArgumentParseException {

        public WorldParseException(final String inputValue) {
            super(inputValue);
        }

        public WorldParseException(final String inputValue, final Throwable cause) {
            super(inputValue, cause);
        }

    }

}