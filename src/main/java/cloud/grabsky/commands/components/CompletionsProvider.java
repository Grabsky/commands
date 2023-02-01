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
package cloud.grabsky.commands.components;

import cloud.grabsky.commands.SimpleCommandContext;
import cloud.grabsky.commands.util.Arrays;

import java.util.List;

import static cloud.grabsky.commands.util.Arrays.toArrayList;

/**
 * {@link CompletionsProvider} is responsible for resolving completions
 * that are later displayed in command input window for executor.
 */
public interface CompletionsProvider {

    List<String> provide(final SimpleCommandContext context);

    /**
     * Returns instance of {@link CompletionsProvider} that provides no completions.
     */
    CompletionsProvider EMPTY = (context) -> Arrays.EMPTY_STRING_LIST;

    static CompletionsProvider of(final List<String> completions) {
        return (context) -> completions;
    }

    static CompletionsProvider of(final String... completions) {
        return (context) -> toArrayList(completions);
    }

    static <T> CompletionsProvider of(final Class<T> type) {
        return (context) -> context.getManager().getSuggestionProvider(type).provide(context);
    }

}
