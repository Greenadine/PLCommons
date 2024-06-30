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

import org.bukkit.event.Event;

import java.lang.annotation.*;

/**
 * An annotation to define a method as a synchronous method. The intention of this annotation is that methods annotated with this can be
 * called asynchronously without the need for manually ensuring that the method is called on the main thread. An example of this would be
 * {@link dev.greenadine.plcommons.BukkitUtils#callEvent(Event)}, which automatically calls the event on the main thread.
 * <p>
 * Annotated methods automatically execute their operations that require the main thread on the main thread. Any operations that do not are
 * either executed on the calling thread, or are in turn executed asynchronously through the Bukkit scheduler, depending on the
 * implementation. Using the same example as before, {@link dev.greenadine.plcommons.BukkitUtils#callEvent(Event)} will call the event on
 * the main thread, but the returned {@link dev.greenadine.plcommons.EventCallback} is constructed and returned on the calling thread.
 * <p>
 * In projects that use this annotation as well as/or {@link Async}, the lack of either generally suggests that the method's operations are
 * executed on the calling thread, and that the caller is responsible for ensuring that the method is called synchronously or asynchronously
 * when desired. This is however <u>not</u> a strict rule, and it is recommended to consult a method's documentation or the implementation
 * itself to determine a method's behavior in relation to synchronous and/or asynchronous execution.
 *
 * @author Greenadine
 * @see Async
 * @since 0.1
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Sync {
}
