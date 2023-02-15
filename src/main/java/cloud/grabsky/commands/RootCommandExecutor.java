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

import cloud.grabsky.commands.exception.IncompatibleSenderException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * {@link RootCommandExecutor} represents command executor.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class RootCommandExecutor {

    private final CommandSender sender;

    /**
     * Returns underlying command executor {@link CommandSender}.
     */
    public @NotNull CommandSender asCommandSender() {
        return sender;
    }

    /**
     * Returns {@code true} if underlying command executor is a {@link Player}.
     */
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    /**
     * Returns underlying command executor cast to {@link Player}.
     *
     * @throws IncompatibleSenderException if executor is not a {@link Player}
     */
    public Player asPlayer() throws IncompatibleSenderException {
        if (sender instanceof Player player)
            return player;
        // ...
        throw new IncompatibleSenderException(Player.class);
    }

    /**
     * Returns {@code true} if underlying command executor is a {@link ConsoleCommandSender}.
     */
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    /**
     * Returns underlying command executor cast to {@link ConsoleCommandSender}.
     *
     * @throws IncompatibleSenderException if executor is not a {@link ConsoleCommandSender}
     */
    public ConsoleCommandSender asConsole() throws IncompatibleSenderException {
        if (sender instanceof ConsoleCommandSender console)
            return console;
        // ...
        throw new IncompatibleSenderException(ConsoleCommandSender.class);
    }

    /**
     * Returns {@code true} if underlying command executor is {@link T}.
     */
    public <T extends CommandSender> boolean is(final Class<T> type) {
        return sender.getClass().isAssignableFrom(type);
    }

    /**
     * Returns underlying command executor cast to {@link ConsoleCommandSender}.
     *
     * @throws IncompatibleSenderException if executor is not a {@link ConsoleCommandSender}
     */
    public <T extends CommandSender> T as(@NotNull final Class<T> type) throws IncompatibleSenderException {
        try {
            return type.cast(sender);
        } catch (final ClassCastException exc) {
            throw new IncompatibleSenderException(type, exc);
        }
    }

}
