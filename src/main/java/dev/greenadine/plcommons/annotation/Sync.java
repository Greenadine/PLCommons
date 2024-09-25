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

import dev.greenadine.plcommons.EventCallback;
import dev.greenadine.plcommons.Events;
import org.bukkit.event.Event;

import java.lang.annotation.*;

/**
 * An annotation to define a method as a synchronous method. The intention of this annotation is that methods annotated with this can be called asynchronously without the need for manually
 * ensuring that the method is called on the main thread. An example of this would be {@link Events#call(Event)}, which automatically calls the event on the
 * main thread.
 * <br><br>
 * Annotated methods automatically execute their operations that require the main thread on the main thread. Any operations that do not require this are either executed on the calling thread,
 * or are in turn executed asynchronously through the Bukkit scheduler, depending on the implementation. For example, {@link Events#callAnd(Event)} will call the event on the main thread, but
 * the {@link EventCallback} is constructed and returned on the calling thread.
 *
 * @see Async
 * @since 0.1
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Sync {
}
