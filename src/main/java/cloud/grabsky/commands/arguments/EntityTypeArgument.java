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
import cloud.grabsky.commands.RootCommandContext;
import cloud.grabsky.commands.components.ArgumentParser;
import cloud.grabsky.commands.exception.ArgumentParseException;
import cloud.grabsky.commands.exception.MissingInputException;
import cloud.grabsky.commands.util.Registries;
import org.bukkit.entity.EntityType;

import java.util.List;

/**
 * Converts literal to {@link EntityType}.
 */
public enum EntityTypeArgument implements CompletionsProvider, ArgumentParser<EntityType> {
    /* SINGLETON */ INSTANCE;

    private static final List<String> MINECRAFT_ENTITY_TYPE_NAMES = Registries.ENTITY_TYPE.keySet().stream()
            .sorted()
            .toList();

    @Override
    public List<String> provide(final RootCommandContext context) {
        return MINECRAFT_ENTITY_TYPE_NAMES;
    }

    @Override
    public EntityType parse(final RootCommandContext context, final ArgumentQueue queue) throws ArgumentParseException, MissingInputException {
        final String value = queue.next();
        final EntityType enchantment = Registries.ENTITY_TYPE.get(value);
        // ...
        if (enchantment != null)
            return enchantment;
        // ...
        throw new EntityTypeParseException(value);
    }

    /**
     * {@link EntityTypeParseException} is thrown when invalid key is provided for {@link EntityType} argument type.
     */
    public static final class EntityTypeParseException extends ArgumentParseException {

        public EntityTypeParseException(final String inputValue) {
            super(inputValue);
        }

        public EntityTypeParseException(final String inputValue, final Throwable cause) {
            super(inputValue, cause);
        }

    }

}