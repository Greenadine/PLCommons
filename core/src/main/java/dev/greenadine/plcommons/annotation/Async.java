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

package dev.greenadine.plcommons.annotation;

import dev.greenadine.plcommons.BukkitUtils;

import java.lang.annotation.*;

/**
 * An annotation for defining a method as an asynchronous method. The intention of this annotation is so that developers can clearly
 * indicate and recognize that a method is inherently asynchronous, instead of for example using a naming convention or mentioning it in the
 * method's JavaDoc or other documentation. Methods annotated with this annotation can be called from any thread without the need for
 * manually ensuring that the method is called asynchronously, as per their design. An example of this would be
 * {@link BukkitUtils#runAsync(Runnable)}, which automatically runs a {@link Runnable} asynchronously through Bukkit's scheduler.
 * <p>
 * Methods annotated with {@code Async} are designed to automatically execute their operations asynchronously. If there are any operations
 * that are required to be executed on the main thread, it is the responsibility of either the implementor or the caller of the method to
 * ensure that this is done, depending on the implementation. Using the same example as before, {@code BukkitUtils#runAsync(Runnable)} will
 * run a given {@code Runnable} asynchronously, but if the {@code Runnable} contains operations that require the main thread, it is the
 * responsibility of the <i>caller</i> to ensure that these operations are instead executed on the main thread.
 * <p>
 * In projects that use this annotation as well as/or {@link Sync}, the lack of either generally suggests that the method's operations are
 * executed on the calling thread, and that the caller is responsible for ensuring that the method is called synchronously or asynchronously
 * when desired. This is however <u>not</u> a strict rule, and it is recommended to consult a method's documentation or the implementation
 * itself to determine a method's behavior in relation to synchronous and/or asynchronous execution.
 *
 * @author Greenadine
 * @see Sync
 * @since 0.1
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Async {
}
