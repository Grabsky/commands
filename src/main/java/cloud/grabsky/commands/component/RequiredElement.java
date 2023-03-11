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

import cloud.grabsky.commands.exception.CommandLogicException;
import org.jetbrains.annotations.NotNull;

public interface RequiredElement<T> {

    /**
     * Parses and returns {@link T} or throws {@link CommandLogicException} on failure.
     */
    @NotNull T asRequired() throws CommandLogicException;

    /**
     * Parses and returns {@link T} or in case of failure, throws {@link CommandLogicException} created using specified {@link ExceptionHandler.Factory}.
     */
    @NotNull <E extends CommandLogicException> T asRequired(final @NotNull ExceptionHandler.Factory factory) throws CommandLogicException;

}
