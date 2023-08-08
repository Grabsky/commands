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

import cloud.grabsky.commands.condition.Condition;
import cloud.grabsky.commands.exception.CommandLogicException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * {@link RootCommandContext} represents command execution details.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class RootCommandContext {

    @Getter(AccessLevel.PUBLIC)
    private final RootCommandManager manager;

    @Getter(AccessLevel.PUBLIC)
    private final RootCommand command;

    @Getter(AccessLevel.PUBLIC)
    private final RootCommandExecutor executor;

    @Getter(AccessLevel.PUBLIC)
    private final RootCommandInput input;

    public String getLabel() {
        return input.getLabel();
    }

    /**
     * Tests provided condition with (this) {@link RootCommandContext}. When failed,  are executed upon failure.
     */
    @Experimental
    public void testCondition(final @NotNull Condition condition) throws CommandLogicException {
        if (condition.test(this) == false)
            throw CommandLogicException.asFinal((___0, ___1) -> condition.accept(this));
    }

    /**
     * Tests provided condition with (this) {@link RootCommandContext}. No further instructions are executed upon failure.
     */
    @Experimental
    public void testCondition(final @NotNull Condition condition, final @NotNull Consumer<RootCommandContext> onFailure) throws CommandLogicException {
        if (condition.test(this) == false)
            throw CommandLogicException.asFinal((___0, ___1) -> onFailure.accept(this));
    }

}

