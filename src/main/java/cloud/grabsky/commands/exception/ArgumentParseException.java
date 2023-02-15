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
import cloud.grabsky.commands.RootCommandExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * {@link ArgumentParseException} is base exception class for all parser-specific errors.
 *
 * @apiNote This exception is safe to be thrown within command logic and <b><u>should not</u></b> be manually handled using {@code try...catch} block.
 */
public class ArgumentParseException extends CommandLogicException {

    @Getter(AccessLevel.PUBLIC)
    public final String inputValue;

    public ArgumentParseException(final String inputValue) {
        super();
        this.inputValue = inputValue;
    }

    public ArgumentParseException(final String inputValue, final Throwable cause) {
        super(cause);
        this.inputValue = inputValue;
    }

    /**
     * Returns final (non-overridable) {@link ArgumentParseException} which sends
     * {@link Component} (error) message to {@link RootCommandExecutor}.
     *
     * @apiNote This is internal API that can change at any time.
     */
    @Internal
    public static ArgumentParseException asReply(final Component message) {
        return new ArgumentParseException(null) {

            @Override
            public boolean isHandlerFinal() {
                return true;
            }

            @Override
            public void accept(final RootCommandContext context) {
                context.getExecutor().asCommandSender().sendMessage(message);
            }

        };
    }

}
