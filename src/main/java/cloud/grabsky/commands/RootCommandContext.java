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

import cloud.grabsky.commands.components.CommandCondition;
import cloud.grabsky.commands.exception.CommandConditionException;
import cloud.grabsky.commands.exception.CommandLogicException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus.Experimental;

import java.lang.reflect.InvocationTargetException;

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


    /* PERMISSION CHECKING */

    @Experimental
    public void requirePermission(final String permission) throws CommandConditionException {
        if (executor.asCommandSender().hasPermission(permission) == false)
            throw CommandConditionException.asReply(Bukkit.permissionMessage());
    }

    @Experimental
    public void requirePermission(final String permission, final Component error) throws CommandConditionException {
        if (executor.asCommandSender().hasPermission(permission) == false)
            throw CommandConditionException.asReply(error);
    }

    /* CONDITION CHECKING - TRUE */

    @Experimental
    public void requireTrue(final CommandCondition condition) throws CommandConditionException {
        if (condition.test(this) == false)
            throw new CommandConditionException();
    }

    @Experimental
    public void requireTrue(final CommandCondition condition, final Component error) throws CommandConditionException {
        if (condition.test(this) == false)
            throw CommandConditionException.asReply(error);
    }

    @Experimental
    public void requireTrue(final Class<? extends CommandCondition> condition) throws CommandLogicException {
        try {
            this.requireTrue(condition.getConstructor().newInstance());
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exc) {
            exc.printStackTrace();
            throw new CommandLogicException(exc);
        }
    }

    @Experimental
    public void requireTrue(final Class<? extends CommandCondition> condition, final Component error) throws CommandLogicException {
        try {
            this.requireTrue(condition.getConstructor().newInstance(), error);
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exc) {
            exc.printStackTrace();
            throw new CommandLogicException(exc);
        }
    }

    /* CONDITION CHECKING - FALSE */

    @Experimental
    public void requireFalse(final CommandCondition condition) throws CommandConditionException {
        if (condition.test(this) == true)
            throw new CommandConditionException();
    }

    @Experimental
    public void requireFalse(final CommandCondition condition, final Component error) throws CommandConditionException {
        if (condition.test(this) == true)
            throw CommandConditionException.asReply(error);
    }

    @Experimental
    public void requireFalse(final Class<? extends CommandCondition> condition) throws CommandLogicException {
        try {
            this.requireFalse(condition.getConstructor().newInstance());
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exc) {
            exc.printStackTrace();
            throw new CommandLogicException(exc);
        }
    }

    @Experimental
    public void requireFalse(final Class<? extends CommandCondition> condition, final Component error) throws CommandLogicException {
        try {
            this.requireFalse(condition.getConstructor().newInstance(), error);
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exc) {
            exc.printStackTrace();
            throw new CommandLogicException(exc);
        }
    }

}

