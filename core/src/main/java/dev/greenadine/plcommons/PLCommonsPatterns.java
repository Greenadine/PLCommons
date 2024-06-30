/*
 * Copyright 2024 Kevin Zuman
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.greenadine.plcommons;

import co.aikar.commands.lib.expiringmap.ExpirationPolicy;
import co.aikar.commands.lib.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PLCommonsPatterns {

    // Example:
    // {1:You have joined world} {2:%world_name%}!
    public static final Pattern COLORS = Pattern.compile("\\{(?<code>\\d+):(?<msg>.*?)}");
    public static final Pattern I18N_STRING = Pattern.compile("\\{@@(?<key>.+?)}", Pattern.CASE_INSENSITIVE);

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
    public static Pattern getPattern(String pattern) {
        return patternCache.computeIfAbsent(pattern, (s) -> Pattern.compile(pattern));
    }
}
