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

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static dev.greenadine.plcommons.BukkitUtils.*;

/**
 * Basic implementation of {@link EventCallback}.
 *
 * @since 0.1
 */
class EventCallbackImpl implements EventCallback {

    private final boolean cancellable;
    private final boolean cancelled;

    EventCallbackImpl(@NotNull Event event) {
        this.cancellable = event instanceof Cancellable;
        this.cancelled = cancellable && ((Cancellable) event).isCancelled();
    }

    @Override
    public @NotNull EventCallback andThen(@NotNull Runnable runnable) {
        if (!cancelled)
            runnable.run();
        return this;
    }

    @Override
    public @NotNull EventCallback andThenSync(@NotNull Runnable runnable) {
        if (!cancelled)
            runSync(runnable);
        return this;
    }

    @Override
    public @NotNull EventCallback andThenAsync(@NotNull Runnable runnable) {
        if (!cancelled)
            runAsync(runnable);
        return this;
    }

    @Override
    public @NotNull EventCallback andThen(@NotNull Runnable runnable, long delay) {
        if (!cancelled) {
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        return this;
    }

    @Override
    public @NotNull EventCallback andThenSync(@NotNull Runnable runnable, long delay) {
        if (!cancelled)
            runLater(runnable, delay);
        return this;
    }

    @Override
    public @NotNull EventCallback andThenAsync(@NotNull Runnable runnable, long delay) {
        if (!cancelled)
            runLaterAsync(runnable, delay);
        return this;
    }

    @Override
    public @NotNull EventCallback onCancel(@NotNull Runnable runnable) {
        if (!cancellable)
            throw new IllegalStateException("Event is not cancellable");
        if (cancelled)
            runnable.run();
        return this;
    }

    @Override
    public @NotNull EventCallback onCancelSync(@NotNull Runnable runnable) {
        if (!cancellable)
            throw new IllegalStateException("Event is not cancellable");
        if (cancelled)
            runSync(runnable);
        return this;
    }

    @Override
    public @NotNull EventCallback onCancelAsync(@NotNull Runnable runnable) {
        if (!cancellable)
            throw new IllegalStateException("Event is not cancellable");
        if (cancelled)
            runAsync(runnable);
        return this;
    }
}
