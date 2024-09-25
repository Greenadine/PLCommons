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

import co.aikar.locales.MessageKey;

final class PLCMessageKeys {

    // The following message keys are already present in the library's own language files
    static final MessageKey PLUGIN_PREFIX = MessageKey.of("pl-commons.plugin_prefix");
    static final MessageKey COMMAND_HELP_HEADER = MessageKey.of("pl-commons.command_help_header");
    static final MessageKey COMMAND_HELP_ENTRY = MessageKey.of("pl-commons.command_help_entry");
    static final MessageKey COMMAND_INVALID_SYNTAX = MessageKey.of("pl-commons.command_invalid_syntax");

    // The following message keys have to be provided by the plugin using the library
    static final MessageKey PLUGIN_NAME = MessageKey.of("pl-commons.plugin_name");
}
