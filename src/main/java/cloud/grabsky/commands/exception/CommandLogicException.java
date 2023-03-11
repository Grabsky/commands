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
package cloud.grabsky.commands.exception;

import cloud.grabsky.commands.RootCommandContext;
import cloud.grabsky.commands.component.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.jetbrains.annotations.ApiStatus.Internal;

/**
 * {@link CommandLogicException} is a base exception class that acts as a "safe" return point for commands.
 *
 * @apiNote This exception is safe to be thrown within command logic and <b><u>should not</u></b> be manually handled using {@code try...catch} block.
 *
 * @see ArgumentParseException
 * @see IncompatibleParserException
 * @see IncompatibleSenderException
 * @see MissingInputException
 */
public class CommandLogicException extends RuntimeException implements Consumer<RootCommandContext> {

    protected CommandLogicException() {
        super();
    }

    public CommandLogicException(final Throwable cause) {
        super(cause);
    }

    @Override
    public void accept(final @NotNull RootCommandContext context) {
        context.getExecutor().asCommandSender().sendMessage(
                text("An error occurred while executing the command. (" + extractClassName(this.getClass()) + ")", RED)
        );
    }

    /**
     * Returns {@code true} if handler is marked as final (non-overridable).
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal
    public boolean isHandlerFinal() {
        return false;
    }

    /**
     * Returns {@link E} with specified {@link ExceptionHandler ExceptionHandler&lt;E&gt;} to be used.
     * Exceptions created using this method are marked as final (non-overridable).
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal @SuppressWarnings("unchecked")
    public static <E extends CommandLogicException> E asFinal(final ExceptionHandler<E> handler) {
        return (E) new CommandLogicException() {

            @Override
            public boolean isHandlerFinal() {
                return true;
            }

            @Override
            public void accept(final @NotNull RootCommandContext context) {
                handler.handle((E) this, context);
            }

        };
    }

    // Returns (simple) class name respecting its' outer classes.
    private static @NotNull String extractClassName(final @NotNull Class<?> clazz) {
        final StringBuilder builder = new StringBuilder(clazz.getSimpleName());
        // ...
        Class<?> outer = clazz.getDeclaringClass();
        // ...
        while (outer != null) {
            builder.insert(0, ".").insert(0, outer.getSimpleName());
            // ...
            outer = outer.getDeclaringClass();
        }
        return builder.toString();
    }

}
