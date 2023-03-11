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
import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Converts three {@link String} literals to {@link Position}.
 * 
 * @apiNote This is experimental API that can change at any time.
 */
@Experimental // Inheriting @Experimental status from Paper
public enum PositionArgument implements CompletionsProvider, ArgumentParser<Position> {
    /* SINGLETON */ INSTANCE;

    @Override
    public @NotNull List<String> provide(final @NotNull RootCommandContext context) {
        final Location location = context.getExecutor().asPlayer().getLocation();
        return Collections.singletonList(new StringBuilder()
                .append(toRoundedDouble(location.x()))
                .append(" ")
                .append(toRoundedDouble(location.y()))
                .append(" ")
                .append(toRoundedDouble(location.z()))
                .toString()
        );
    }

    @Override
    public Position parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws ArgumentParseException, MissingInputException {
        final Double x = arguments.next(Double.class).asOptional();
        final Double y = arguments.next(Double.class).asOptional();
        final Double z = arguments.next(Double.class).asOptional();
        // ...
        if (x != null && y != null && z != null)
            return Position.fine(x, y, z);
        // ...
        throw new PositionArgument.Exception(x + " " + y + " " + z);
    }

    private static String toRoundedDouble(final double num) {
        return String.format("%.2f", num);
    }

    /**
     * {@link Exception} is thrown when invalid coordinates are provided for {@link Position} argument type.
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