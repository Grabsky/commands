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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public enum ComponentArgument implements ArgumentParser<Component> {

    /**
     * Returns next argument of {@link ArgumentQueue} as {@link Component} parsed using (default) {@link MiniMessage} serializer.
     */
    LITERAL {

        private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

        @Override
        public Component parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws MissingInputException {
            return MINI_MESSAGE.deserialize(arguments.nextString());
        }

    },

    /**
     * Returns remaining arguments of {@link ArgumentQueue} as {@link Component} parsed using (default) {@link MiniMessage} serializer.
     */
    GREEDY {

        private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

        @Override
        public Component parse(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws MissingInputException {
            final StringBuilder builder = new StringBuilder(arguments.nextString());
            // appending arguments till the end of input
            while (arguments.hasNext() == true) {
                builder.append(" ").append(arguments.nextString());
            }
            // serializing and returning
            return MINI_MESSAGE.deserialize(builder.toString());
        }

    }

}
