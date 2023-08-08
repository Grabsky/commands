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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.Acceleration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents command input supplied by the executor.
 */
public final class RootCommandInput {

    @Getter(AccessLevel.PUBLIC)
    private final String input;

    @Getter(AccessLevel.PUBLIC)
    private final String label;

    @Accessors(fluent = true)
    @Getter(AccessLevel.PUBLIC)
    private final int length;

    @Accessors(fluent = true)
    @Getter(AccessLevel.PUBLIC)
    private final int maxIndex;

    private final String[] arguments;

    /* PACKAGE PRIVATE */ RootCommandInput(final @NotNull String label, final @NotNull String[] rawArguments) {
        this.label = label;
        this.input = toStringInput(label, rawArguments);
        this.arguments = this.input.split(" ");
        this.length = arguments.length;
        this.maxIndex = Math.max(0, arguments.length - 1);
    }

    /**
     * Returns {@link String} argument at specified index, or {@code null} if out of bounds.
     */
    public @Nullable String at(final int index) {
        return (index < arguments.length) ? arguments[index] : null;
    }

    /**
     * Returns {@link String} argument at specified index, or provided {@code def} if out of bounds.
     */
    public @UnknownNullability String at(final int index, final @Nullable String def) {
        return (index < arguments.length) ? arguments[index] : def;
    }

    @Override
    public String toString() {
        return input;
    }

    /* STATIC HELPERS */

    private static String toStringInput(final String label, final String[] arguments) {
        final StringBuilder input = new StringBuilder(label);
        // ...
        for (final String argument : arguments)
            if (argument != null && argument.isEmpty() == false)
                input.append(" ").append(argument);
        // ...
        return input.toString();
    }

}
