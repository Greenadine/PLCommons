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

public final class Strings {

    private Strings() {
    }

    /**
     * Checks if the given string equals any of the given strings.
     *
     * @param string  the string to check.
     * @param strings the strings to compare.
     * @return true if the string equals any of the given strings, false otherwise.
     */
    public static boolean equalsAny(String string, String... strings) {
        for (final String str : strings)
            if (string.equals(str))
                return true;
        return false;
    }
}
