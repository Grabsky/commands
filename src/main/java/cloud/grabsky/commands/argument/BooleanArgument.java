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
import cloud.grabsky.commands.exception.CommandLogicException;
import cloud.grabsky.commands.exception.MissingInputException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Converts {@link String} literal to {@link Boolean}.
 */
public enum BooleanArgument implements CompletionsProvider, ArgumentParser<Boolean> {
    /* SINGLETON */ INSTANCE;

    private static final List<String> BOOLEAN_NAMES = List.of("true", "false");

    @Override
    public @NotNull List<String> provide(final @NotNull RootCommandContext context) throws CommandLogicException {
        return BOOLEAN_NAMES;
    }

    @Override
    public Boolean parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws ArgumentParseException, MissingInputException {
        final String value = arguments.nextString();
        // ...
        return switch (value.toLowerCase()) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new BooleanArgument.Exception(value);
        };
    }

    /**
     * {@link Exception} is thrown when invalid value is provided for {@link Boolean} argument type.
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