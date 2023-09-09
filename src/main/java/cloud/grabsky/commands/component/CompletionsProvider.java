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
package cloud.grabsky.commands.component;

import cloud.grabsky.commands.RootCommandContext;
import cloud.grabsky.commands.exception.CommandLogicException;
import cloud.grabsky.commands.util.Arrays;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cloud.grabsky.commands.util.Arrays.toArrayList;

/**
 * {@link CompletionsProvider} is responsible for resolving completions
 * that are later displayed in command input window for executor.
 */
public interface CompletionsProvider {

    /**
     * Returns instance of {@link CompletionsProvider} that provides no completions.
     */
    CompletionsProvider EMPTY = (context) -> Arrays.EMPTY_STRING_LIST;

    // TO-DO: Javadocs
    static @NotNull CompletionsProvider of(final List<String> completions) {
        return (context) -> completions;
    }

    // TO-DO: Javadocs
    static @NotNull CompletionsProvider of(final String... completions) {
        return (context) -> toArrayList(completions);
    }

    /**
     * Returns instance of {@link CompletionsProvider} created out of specified elements.
     * This method handles elements that are either {@link String} or {@link Class} where the second
     * is used to query completions using {@link CompletionsProvider} registered for that type.
     * Elements of other types are ignored.
     *
     * @apiNote This is experimental API that can change at any time.
     */
    // TO-DO: Replace with JDK 21 enhanced switch.
    @Experimental @SuppressWarnings("unchecked")
    static @NotNull CompletionsProvider of(final Object... any) {
        return (context) -> {
            final ArrayList<String> result = new ArrayList<>();
            // ...
            for (final Object element : any) {
                if (element instanceof Class<?> type)
                    result.addAll(context.getManager().getCompletionsProvider(type).provide(context));
                else if (element instanceof String completion)
                    result.add(completion);
                else if (element instanceof String[] completions)
                    Collections.addAll(result, completions);
                else if (element instanceof Collection<?> completions && completions.isEmpty() == false && completions.iterator().next() instanceof String)
                    result.addAll((Collection<String>) completions);
            }
            // ...
            return result;
        };
    }

    // TO-DO: Javadocs
    static @NotNull CompletionsProvider of(final Class<?> type) {
        return (context) -> context.getManager().getCompletionsProvider(type).provide(context);
    }

    // TO-DO: Javadocs
    static @NotNull CompletionsProvider filtered(final Predicate<String> predicate, final String... any) {
        return (context) -> Stream.of(any).filter(predicate).toList();
    }

    // TO-DO: Javadocs
    static @NotNull CompletionsProvider filtered(final Predicate<String> predicate, final List<String> completions) {
        return (context) -> completions.stream().filter(predicate).toList();
    }

    @NotNull List<String> provide(final @NotNull RootCommandContext context) throws CommandLogicException;

}
