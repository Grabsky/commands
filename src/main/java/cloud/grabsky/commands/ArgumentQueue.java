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

import cloud.grabsky.commands.components.ArgumentParser;
import cloud.grabsky.commands.exception.MissingInputException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * {@link ArgumentQueue} contains all arguments provided for the executed command and exposes
 * convenient methods that wraps them in {@link Argument Argument&lt;T&gt;} of specific type.
 *
 * @apiNote Operating on {@link ArgumentQueue} is not thread-safe.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class ArgumentQueue {

    private final SimpleCommandContext context;
    private final List<String> arguments;

    /**
     * Returns {@code true} if at least one more element is present in the internal arguments array.
     */
    public boolean hasNext() {
        return arguments.isEmpty() == false;
    }

    /**
     * Returns {@link Argument Argument&lt;T&gt;} wrapper for specified {@link T} type.
     * <br />
     * <br />
     * You should process the argument <b><u>immediately</u></b> after it is returned.
     * <br />
     * <br />
     * <pre>
     * // DO NOT DO THAT
     * final Argument&lt;Player&gt; arg0 = queue.next(Player.class);
     * final Argument&lt;Player&gt; arg1 = queue.next(Player.class);
     *
     * // DO THAT INSTEAD
     * final Player arg0 = queue.next(Player.class).get();
     * final Player arg1 = queue.next(Player.class).get();
     * </pre>
     * Doing otherwise, will most likely cause loss of the desired argument order.
     */
    public <T> Argument<T> next(final Class<T> type) {
        return new Argument<T>(type, context, this);
    }

    /**
     * Returns {@link Argument Argument&lt;T&gt;} wrapper for specified {@link T} type,
     * with exclusively specified {@link ArgumentParser ArgumentParser&lt;T&gt;}.
     * <br />
     * <br />
     * You should process the argument <b><u>immediately</u></b> after it is returned.
     * <br />
     * <br />
     * <pre>
     * // DO NOT DO THAT
     * final Argument&lt;Player&gt; arg0 = queue.next(Player.class);
     *
     * // DO THAT INSTEAD
     * final Player arg0 = queue.next(Player.class).get();
     * </pre>
     * Doing otherwise, will most likely cause loss of the desired argument order.
     */
    public <T> Argument<T> next(final Class<T> type, final ArgumentParser<T> parser) {
        return new Argument<>(type, context, this, parser);
    }

    /**
     * Returns and removes next {@link String} at the beginning of the this {@link ArgumentQueue}.
     */
    public String next() throws MissingInputException {
        try {
            return arguments.remove(0);
        } catch (final IndexOutOfBoundsException exc) {
            throw new MissingInputException(exc);
        }
    }

}
