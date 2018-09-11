package net.survival.concurrent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public final class TaskScheduler
{
    private static final Queue<CoroutineTask<?>> tasks = new LinkedList<>();

    private TaskScheduler() {}

    public static boolean pollTasks() {
        return !tasks.isEmpty();
    }

    public static void dispatchTasks() {
        Iterator<CoroutineTask<?>> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            CoroutineTask<?> task = iterator.next();

            if (task.next())
                iterator.remove();
        }
    }

    static <T> void pushTask(CoroutineTask<T> task) {
        tasks.add(task);
    }
}