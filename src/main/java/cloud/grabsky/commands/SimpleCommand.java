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

import cloud.grabsky.commands.components.CompletionsProvider;
import cloud.grabsky.commands.exception.CommandLogicException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * {@link SimpleCommand} represents a server command.
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class SimpleCommand {

    @Getter(AccessLevel.PUBLIC)
    private final String name;

    @Getter(AccessLevel.PUBLIC)
    private final @Nullable String[] aliases;

    @Getter(AccessLevel.PUBLIC)
    private final @Nullable String permission;

    @Getter(AccessLevel.PUBLIC)
    private final @Nullable String usage;

    @Getter(AccessLevel.PUBLIC)
    private final @Nullable String description;

    /**
     * Handles command completions/suggestions that pop-up for the client.
     *
     * @apiNote {@link CommandLogicException} should not be caught here.
     */
    public CompletionsProvider onTabComplete(final SimpleCommandContext context, final int index) {
        return CompletionsProvider.EMPTY; // commands have no completions by default
    }

    /**
     * Handles command logic that is called upon command execution.
     *
     * @apiNote {@link CommandLogicException} should not be caught here.
     */
    public abstract void onCommand(final SimpleCommandContext context, final ArgumentQueue arguments) throws CommandLogicException;

}
