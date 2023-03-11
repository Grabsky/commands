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

import cloud.grabsky.commands.component.ArgumentParser;
import cloud.grabsky.commands.component.ExceptionHandler;
import cloud.grabsky.commands.component.OptionalElement;
import cloud.grabsky.commands.component.RequiredElement;
import cloud.grabsky.commands.exception.ArgumentParseException;
import cloud.grabsky.commands.exception.CommandLogicException;
import cloud.grabsky.commands.exception.MissingInputException;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Argument Argument&lt;T&gt;} represents intermediary type between user input and desired value.
 */
public final class Argument<T> implements RequiredElement<T>, OptionalElement<T> {

    @Getter(AccessLevel.PUBLIC)
    private final Class<T> type;

    private final RootCommandContext context;
    private final ArgumentParser<T> parser;
    private final ArgumentQueue queue;

    public Argument(final @NotNull Class<T> type, final @NotNull RootCommandContext context, final @Nullable ArgumentParser<T> parser, final @NotNull ArgumentQueue queue) {
        this.type = type;
        this.context = context;
        this.parser = (parser == null) ? context.getManager().getArgumentParser(type) : parser;
        this.queue = queue;
    }

    @Override
    public @NotNull T asRequired(final @NotNull ExceptionHandler.Factory factory) throws CommandLogicException {
        try {
            return this.asRequired();
        } catch (final MissingInputException | ArgumentParseException cause) {
            final ExceptionHandler<?> handler = factory.create(cause);
            // Using inline exception handler if provided
            if (handler != null)
                throw CommandLogicException.asFinal(handler);
            // Re-throwing so RootCommandManager can handle that.
            throw cause;
        }
    }

    @Override
    public @NotNull T asRequired() throws ArgumentParseException, MissingInputException {
        return parser.parse(context, queue);
    }

    @Override
    public @Nullable T asOptional() {
        return this.asOptional(null);
    }

    @Override
    public T asOptional(final T def) throws ArgumentParseException {
        try {
            return this.asRequired();
        } catch (final MissingInputException cause) {
            return def;
        }
    }

}
