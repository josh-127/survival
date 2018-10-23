package net.survival.concurrent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public final class FiberScheduler
{
    private static final Queue<Fiber> fibers = new LinkedList<>();

    private FiberScheduler() {}

    public static boolean poll() {
        return !fibers.isEmpty();
    }

    public static void dispatch() {
        Iterator<Fiber> iterator = fibers.iterator();

        while (iterator.hasNext()) {
            Fiber fiber = iterator.next();

            if (fiber.next())
                iterator.remove();
        }
    }

    static <T> void pushFiber(Fiber fiber) {
        fibers.add(fiber);
    }
}