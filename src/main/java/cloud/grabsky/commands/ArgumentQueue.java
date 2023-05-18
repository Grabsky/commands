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
import cloud.grabsky.commands.exception.MissingInputException;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static java.util.Collections.unmodifiableList;
import static org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * {@link ArgumentQueue} contains all arguments provided for the executed command and exposes
 * convenient methods that wraps them in {@link Argument Argument&lt;T&gt;} of specific type.
 *
 * @apiNote Operating on {@link ArgumentQueue} is not thread-safe.
 */
public final class ArgumentQueue {

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull RootCommandContext context;

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull @Unmodifiable List<String> arguments;

    // NOTE: Iterator should not be directly exposed to the API.
    private final @NotNull ListIterator<String> iterator;

    /* package private */ ArgumentQueue(final @NotNull RootCommandContext context, final @NotNull List<String> arguments) {
        this.context = context;
        this.arguments = unmodifiableList(arguments);
        this.iterator = arguments.listIterator();
    }

    /**
     * Returns {@code true} if at least one more element is present.
     */
    public boolean hasNext() {
        return iterator.hasNext() == true;
    }

    /**
     * Returns next index of this {@link ArgumentQueue}.
     */
    public int getNextIndex() {
        return iterator.nextIndex();
    }

    /**
     * Returns next {@link String} at the beginning of this {@link ArgumentQueue}.
     */
    public @NotNull String nextString() throws MissingInputException {
        try {
            return iterator.next();
        } catch (final NoSuchElementException e) {
            throw new MissingInputException(e);
        }
    }

    /**
     * Returns next element at the beginning of this {@link ArgumentQueue}
     * as an {@link Argument Argument&lt;T&gt;} wrapper for specified {@link T} {@code type}.
     * <br />
     * <br />
     * You should process the argument <b><u>immediately</u></b> after it is returned.
     * <br />
     * <br />
     * <pre>
     * // DO NOT DO THAT
     * final Argument&lt;Player&gt; arg = arguments.next(Player.class);
     *
     * // DO THAT INSTEAD
     * final Player target = arguments.next(Player.class).asRequired();
     * </pre>
     * Doing otherwise, may cause loss of the desired argument order.
     */
    public <T> @NotNull Argument<T> next(final @NotNull Class<T> type) {
        return new Argument<>(type, context, null, this);
    }

    /**
     * Returns next element at the beginning of this {@link ArgumentQueue},
     * as an {@link Argument Argument&lt;T&gt;} wrapper for specified {@link T} {@code type},
     * with exclusively specified {@link ArgumentParser ArgumentParser&lt;T&gt;} {@code parser}.
     * <br />
     * <br />
     * You should process the argument <b><u>immediately</u></b> after it is returned.
     * <br />
     * <br />
     * <pre>
     * // DO NOT DO THAT
     * final Argument&lt;Player&gt; arg = arguments.next(Player.class);
     *
     * // DO THAT INSTEAD
     * final Player target = arguments.next(Player.class).asRequired();
     * </pre>
     * Doing otherwise, may cause loss of the desired argument order.
     */
    public <T> @NotNull Argument<T> next(final @NotNull Class<T> type, final @NotNull ArgumentParser<T> parser) {
        return new Argument<>(type, context, parser, this);
    }

    /**
     * Returns a copy of original {@link ArgumentQueue}.
     *
     * @apiNote This is experimental API that can change at any time.
     */
    @Experimental
    public @NotNull ArgumentQueue original() {
        return new ArgumentQueue(context, arguments);
    }

    /**
     * Returns a copy of (this) {@link ArgumentQueue}.
     *
     * @apiNote This is experimental API that can change at any time.
     */
    @Experimental
    public @NotNull ArgumentQueue peek() {
        final ArgumentQueue queue = new ArgumentQueue(context, arguments);
        // ...
        while (queue.iterator.nextIndex() != this.iterator.nextIndex())
            queue.iterator.next();
        // ...
        return queue;
    }

}
