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
import cloud.grabsky.commands.components.ArgumentParser;
import cloud.grabsky.commands.components.OptionalElement;
import cloud.grabsky.commands.exception.ArgumentParseException;
import cloud.grabsky.commands.exception.MissingInputException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class Argument<T> implements RequiredElement<T>, OptionalElement<T> {

    private final Class<T> type;
    private final SimpleCommandContext context;
    private final ArgumentQueue queue;
    private final ArgumentParser<T> parser; // can be null

    public Argument(final Class<T> type, final SimpleCommandContext context, final ArgumentQueue queue) {
        this.type = type;
        this.context = context;
        this.queue = queue;
        this.parser = null;
    }

    @Override
    public T asRequired() throws ArgumentParseException {
        return (parser != null) ? parser.parse(context, queue) : context.getManager().getArgumentParser(type).parse(context, queue);
    }

    @Override
    public T asRequired(final Component error) throws ArgumentParseException, MissingInputException {
        try {
            return this.asRequired();
        } catch (final ArgumentParseException exc) {
            throw ArgumentParseException.asReply(error);
        } catch (final MissingInputException exc) {
            throw MissingInputException.asReply(error);
        }
    }

    @Override
    public @Nullable T asOptional() {
        return this.asOptional(null);
    }

    @Override
    public T asOptional(final T def) {
        try {
            return this.asRequired();
        } catch (final MissingInputException | ArgumentParseException cause) {
            return def;
        }
    }

}
