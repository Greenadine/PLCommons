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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class PLCommonsConfigurations {

    private static final Map<Class<?>, Serializer<?, ?>> serializers = new HashMap<>();

    public static void load(@NotNull Class<?> clazz) {
        // TODO: Implement
    }

    private static void registerSerializer(@NotNull Class<?> clazz, @NotNull Serializer<?, ?> serializer) {
        serializers.put(clazz, serializer);
    }
}
