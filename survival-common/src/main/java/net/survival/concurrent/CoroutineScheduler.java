package net.survival.concurrent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public final class CoroutineScheduler
{
    private static final Queue<Coroutine<?>> coroutines = new LinkedList<>();

    private CoroutineScheduler() {}

    public static boolean poll() {
        return !coroutines.isEmpty();
    }

    public static void dispatch() {
        Iterator<Coroutine<?>> iterator = coroutines.iterator();

        while (iterator.hasNext()) {
            Coroutine<?> coroutine = iterator.next();

            if (coroutine.next())
                iterator.remove();
        }
    }

    static <T> void pushCoroutine(Coroutine<T> coroutine) {
        coroutines.add(coroutine);
    }
}