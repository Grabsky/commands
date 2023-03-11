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
import cloud.grabsky.commands.util.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Converts {@link String} literal to {@link EntityType}.
 */
public enum EntityTypeArgument implements CompletionsProvider, ArgumentParser<EntityType> {
    /* SINGLETON */ INSTANCE;

    private static final List<String> MINECRAFT_ENTITY_TYPE_NAMES = Registries.ENTITY_TYPE.keySet().stream()
            .sorted()
            .toList();

    @Override
    public @NotNull List<String> provide(final @NotNull RootCommandContext context) {
        return MINECRAFT_ENTITY_TYPE_NAMES;
    }

    @Override
    public EntityType parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws ArgumentParseException, MissingInputException {
        final String value = arguments.nextString();
        final NamespacedKey key = NamespacedKey.fromString(value);
        // ...
        if (key != null) {
            final EntityType entity = Registry.ENTITY_TYPE.get(key);
            if (entity != null)
                return entity;
        }
        throw new EntityTypeArgument.Exception(value);
    }

    /**
     * {@link Exception} is thrown when invalid key is provided for {@link EntityType} argument type.
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