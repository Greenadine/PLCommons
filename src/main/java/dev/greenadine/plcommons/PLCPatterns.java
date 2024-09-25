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

import co.aikar.commands.lib.expiringmap.ExpirationPolicy;
import co.aikar.commands.lib.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

class PLCPatterns {

    // Example:
    // {1:You have joined world} {2:%world_name%}!
    static final Pattern COLOR_FORMATTER = Pattern.compile("\\{(?<code>[1-9a-o]{1,2}):(?<msg>.*?)}");
    static final Pattern I18N_STRING = Pattern.compile("\\{@@(?<key>.+?)}", Pattern.CASE_INSENSITIVE);

    static final Map<String, Pattern> patternCache;
    static {
        patternCache = ExpiringMap.builder().maxSize(200).expiration(1L, TimeUnit.HOURS).expirationPolicy(ExpirationPolicy.ACCESSED).build();
    }

    /**
     * Gets a pattern and compiles it. If the pattern is already present in the cache, it will be returned. Otherwise, it will be compiled
     * and added to the cache.
     *
     * @param pattern the pattern to get.
     * @return the pattern.
     */
     static Pattern getPattern(String pattern) {
        return patternCache.computeIfAbsent(pattern, (s) -> Pattern.compile(pattern));
    }
}
