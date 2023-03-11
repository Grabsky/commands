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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link ArgumentQueue} contains all arguments provided for the executed command and exposes
 * convenient methods that wraps them in {@link Argument Argument&lt;T&gt;} of specific type.
 *
 * @apiNote Operating on {@link ArgumentQueue} is not thread-safe.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ArgumentQueue {

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull RootCommandContext context;

    private final List<String> arguments;

    @Getter(AccessLevel.PUBLIC)
    private int currentArgumentIndex = 0;

    /**
     * Returns {@code true} if at least one more element is present in the internal arguments array.
     */
    public boolean hasNext() {
        return arguments.isEmpty() == false;
    }

    /**
     * Returns and removes next element at the beginning of this {@link ArgumentQueue}
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
     * Doing otherwise, will most likely cause loss of the desired argument order.
     */
    public <T> @NotNull Argument<T> next(final @NotNull Class<T> type) {
        return new Argument<>(type, context, null, this);
    }

    /**
     * Returns and removes next element at the beginning of this {@link ArgumentQueue},
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
     * Doing otherwise, will most likely cause loss of the desired argument order.
     */
    public <T> @NotNull Argument<T> next(final @NotNull Class<T> type, final @NotNull ArgumentParser<T> parser) {
        return new Argument<>(type, context, parser, this);
    }

    /**
     * Returns and removes next {@link String} at the beginning of the this {@link ArgumentQueue}.
     */
    public @NotNull String nextString() throws MissingInputException {
        try {
            return this.removeNext();
        } catch (final IndexOutOfBoundsException exc) {
            throw new MissingInputException(exc);
        }
    }

    // Wrapped within seperate method so when IndexOutOfBoundsException is thrown, currentArgumentIndex is not increased
    private String removeNext() throws IndexOutOfBoundsException {
        final String next = arguments.remove(0);
        currentArgumentIndex += 1;
        return next;
    }

}
