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

import cloud.grabsky.commands.components.RequiredElement;
import cloud.grabsky.commands.exception.IncompatibleSenderException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class SimpleCommandExecutor {

    private final CommandSender sender;

    public CommandSender raw() {
        return sender;
    }

    public <T extends CommandSender> Wrapper<T> as(@NotNull final Class<T> type) {
        return new Wrapper<T>(sender, type);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Wrapper<T extends CommandSender> implements RequiredElement<T> {

        private final CommandSender sender;
        private final Class<T> type;

        @Override
        public T asRequired() throws IncompatibleSenderException {
            try {
                return type.cast(sender);
            } catch (final ClassCastException exc) {
                throw new IncompatibleSenderException(type, exc);
            }
        }

        @Override
        public T asRequired(final Component error) throws IncompatibleSenderException {
            try {
                return this.asRequired();
            } catch (final IncompatibleSenderException exc) {
                throw IncompatibleSenderException.asReply(error);
            }
        }

    }
}
