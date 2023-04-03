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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Converts three {@link String} literals to {@link Position}.
 * 
 * @apiNote This is experimental API that can change at any time.
 */
@Experimental // Inheriting @Experimental status from Paper.
public enum PositionArgument implements CompletionsProvider, ArgumentParser<Position> {
    /* SINGLETON */ INSTANCE;

    @Override
    public @NotNull List<String> provide(final @NotNull RootCommandContext context) {
        return List.of("@x @y @z");
    }

    @Override
    public Position parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws ArgumentParseException, MissingInputException {
        final @NotNull String valueX = arguments.nextString();
        final @NotNull String valueY = arguments.nextString();
        final @NotNull String valueZ = arguments.nextString();
        // ...
        final @Nullable Player player = (context.getExecutor().isPlayer() == true) ? context.getExecutor().asPlayer() : null;
        // ...
        final Double x = (player != null && "@x".equalsIgnoreCase(valueX) == true) ? (Double) player.getLocation().x() : parseDouble(valueX);
        final Double y = (player != null && "@y".equalsIgnoreCase(valueY) == true) ? (Double) player.getLocation().y() : parseDouble(valueY);
        final Double z = (player != null && "@z".equalsIgnoreCase(valueZ) == true) ? (Double) player.getLocation().z() : parseDouble(valueZ);
        // ...
        if (x == null || y == null || z == null) {
            final String input = new StringBuilder()
                    .append(x != null ? toRoundedDouble(x) : valueX).append(" ")
                    .append(y != null ? toRoundedDouble(y) : valueY).append(" ")
                    .append(z != null ? toRoundedDouble(z) : valueZ)
                    .toString();
            // ...
            throw new PositionArgument.Exception(input);
        }
        // ...
        return Position.fine(x, y, z);
    }

    private static @Nullable Double parseDouble(final @NotNull String value) {
        try {
            return Double.parseDouble(value);
        } catch (final NumberFormatException e) {
            return null;
        }
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