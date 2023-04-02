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
import cloud.grabsky.commands.exception.MissingInputException;
import cloud.grabsky.commands.exception.NumberParseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Converts {@link String} literal to {@link Integer}.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class LongArgument implements ArgumentParser<Long> {

    public static final LongArgument DEFAULT_RANGE = new LongArgument(Long.MIN_VALUE, Long.MAX_VALUE);

    public static ArgumentParser<Long> ofRange(final long min, final long max) {
        return new LongArgument(min, max);
    }

    @Getter(AccessLevel.PUBLIC)
    private final long min;

    @Getter(AccessLevel.PUBLIC)
    private final long max;

    @Override
    public Long parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws NumberParseException, MissingInputException {
        final String value = arguments.nextString();
        try {
            final BigDecimal num = new BigDecimal(value);
            // Throwing an exception in case provided number is out of specified range.
            if (BigDecimal.valueOf(min).compareTo(num) > 0 || BigDecimal.valueOf(max).compareTo(num) < 0)
                throw new LongArgument.RangeException(value, min, max);
            // Returning the number.
            return num.longValue();
        } catch (final NumberFormatException e) {
            throw new LongArgument.ParseException(value, e);
        }
    }

    /**
     * {@link ParseException} is thrown when invalid number is provided for {@link Long} argument type.
     */
    public static final class ParseException extends NumberParseException {

        private ParseException(final String inputValue) {
            super(inputValue);
        }

        private ParseException(final String inputValue, final Throwable cause) {
            super(inputValue, cause);
        }

    }

    /**
     * {@link RangeException} is thrown when provided {@link Long} is out of specified range.
     */
    public static final class RangeException extends NumberParseException {

        @Getter(AccessLevel.PUBLIC)
        private final long min;

        @Getter(AccessLevel.PUBLIC)
        private final long max;

        private RangeException(final String inputValue, final long min, final long max) {
            super(inputValue);
            this.min = min;
            this.max = max;
        }

        private RangeException(final String inputValue, final long min, final long max, final Throwable cause) {
            super(inputValue, cause);
            this.min = min;
            this.max = max;
        }

    }

}
