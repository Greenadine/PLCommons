/*
 * Copyright (C) 2024 Greenadine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.greenadine.plcommons;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * An exception that is thrown when a plugin fails to enable, for whatever reason.
 *
 * @since 0.1
 */
public class PluginEnableException extends Exception {

    final List<String> messageLines;

    /**
     * Constructs a new plugin enable exception with the given message.
     *
     * @param message the message.
     */
    public PluginEnableException(@Nullable String... message) {
        this(null, message);
    }

    /**
     * Constructs a new plugin enable exception with the given message and cause.
     *
     * @param cause the cause.
     * @param message the message.
     */
    public PluginEnableException(@Nullable Throwable cause, @Nullable String... message) {
        super(String.join(" ", message), cause);
        this.messageLines = Arrays.asList(message);
    }
}