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

import cloud.grabsky.commands.component.CompletionsProvider;
import cloud.grabsky.commands.exception.CommandLogicException;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

/**
 * {@link RootCommand} represents a server command.
 */
public abstract class RootCommand {

    /**
     * Empty constructor to be used along with {@link Command @Command} annotation.
     */
    public RootCommand() { /* EMPTY */ }

    /**
     * Default constructor. When {@link Command @Command} is present, this constructor is not called.
     */
    public RootCommand(final @NotNull String name, final @Nullable List<String> aliases, final @Nullable String permission, final @Nullable String usage, final @Nullable String description) {
        this.name = name;
        this.aliases = (aliases != null) ? Collections.unmodifiableList(aliases) : null;
        this.permission = permission;
        this.usage = usage;
        this.description = description;
    }

    @Getter(AccessLevel.PUBLIC)
    private @UnknownNullability String name;

    @Getter(AccessLevel.PUBLIC)
    private @Unmodifiable @Nullable List<String> aliases;

    @Getter(AccessLevel.PUBLIC)
    private @Nullable String permission;

    @Getter(AccessLevel.PUBLIC)
    private @Nullable String usage;

    @Getter(AccessLevel.PUBLIC)
    private @Nullable String description;

    /**
     * Handles command completions/suggestions that pop-up for the client.
     *
     * @apiNote You should not {@code try...catch} any {@link CommandLogicException} thrown by this method.
     */
    public @NotNull CompletionsProvider onTabComplete(final @NotNull RootCommandContext context, final int index) throws CommandLogicException {
        return CompletionsProvider.EMPTY; // Commands have no completions by default.
    }

    /**
     * Handles command logic that is called upon command execution.
     *
     * @apiNote You should not {@code try...catch} any {@link CommandLogicException} thrown by this method.
     */
    public abstract void onCommand(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) throws CommandLogicException;

}
