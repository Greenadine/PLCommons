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

package dev.greenadine.plcommons.annotation;

import dev.greenadine.plcommons.Scheduling;

import java.lang.annotation.*;

/**
 * An annotation for defining a method as an asynchronous method. The intention of this annotation is so that developers can clearly
 * indicate and recognize that a method is inherently asynchronous, instead of for example using a naming convention or mentioning it in the
 * method's JavaDoc or other documentation. Methods annotated with this annotation can be called from any thread without the need for
 * manually ensuring that the method is called asynchronously, as per their design. An example of this would be
 * {@link Scheduling#runAsync(Runnable)}, which automatically runs a {@link Runnable} asynchronously through Bukkit's scheduler.
 * <p>
 * Methods annotated with {@code Async} are designed to automatically execute their operations asynchronously. If there are any operations
 * that are required to be executed on the main thread, it is the responsibility of either the implementor or the caller of the method to
 * ensure that this is done, depending on the implementation. Using the same example as before, {@code BukkitUtils#runAsync(Runnable)} will
 * run a given {@code Runnable} asynchronously, but if the {@code Runnable} contains operations that require the main thread, it is the
 * responsibility of the <i>caller</i> to ensure that these operations are instead executed on the main thread.
 *
 * @see Sync
 * @since 0.1
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Async {
}
