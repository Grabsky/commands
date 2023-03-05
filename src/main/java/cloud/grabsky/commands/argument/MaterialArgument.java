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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.List;

/**
 * Converts {@link String} literal to {@link Material}.
 */
public enum MaterialArgument implements CompletionsProvider, ArgumentParser<Material> {
    /* SINGLETON */ INSTANCE;

    private static final List<String> MINECRAFT_MATERIAL_NAMES = Registries.MATERIAL.keySet().stream()
            .sorted()
            .toList();

    @Override
    public List<String> provide(final RootCommandContext context) {
        return MINECRAFT_MATERIAL_NAMES;
    }

    @Override
    public Material parse(final RootCommandContext context, final ArgumentQueue arguments) throws ArgumentParseException, MissingInputException {
        final String value = arguments.next();
        // ...
        final NamespacedKey key = NamespacedKey.fromString(value);
        // ...
        if (key != null) {
            final Material material = Registry.MATERIAL.get(key);
            if (material != null)
                return material;
        }
        // ...
        throw new MaterialParseException(value);
    }

    /**
     * {@link MaterialParseException MaterialParseException} is thrown when invalid key is provided for {@link Material} argument type.
     */
    public static final class MaterialParseException extends ArgumentParseException {

        public MaterialParseException(final String inputValue) {
            super(inputValue);
        }

        public MaterialParseException(final String inputValue, final Throwable cause) {
            super(inputValue, cause);
        }

    }

}