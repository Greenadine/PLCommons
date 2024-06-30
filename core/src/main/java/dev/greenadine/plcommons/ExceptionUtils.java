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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class for exceptions.
 *
 * @since 0.1
 */
public final class ExceptionUtils {

    /**
     * Returns the stack trace output of a throwable.
     *
     * @param throwable the throwable.
     * @return the stack trace output
     */
    @NotNull
    public static String getStackTraceOutput(Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * Returns the lines of the stack trace output of a throwable.
     *
     * @param throwable the throwable.
     * @return the lines of the stack trace output
     */
    @NotNull
    public static List<String> getStackTraceOutputLines(Throwable throwable) {
        final String stackTraceOutput = getStackTraceOutput(throwable);
        return Arrays.asList(stackTraceOutput.split("\\r?\\n", 0)); // "0" to remove empty trailing parts
    }
}
