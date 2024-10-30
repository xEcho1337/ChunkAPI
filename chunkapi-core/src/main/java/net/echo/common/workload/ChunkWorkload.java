package net.echo.common.workload;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class ChunkWorkload implements Runnable {

    private final Queue<Runnable> tasks;
    private final int maxMsPerTick;

    public ChunkWorkload(int maxMsPerTick) {
        this.maxMsPerTick = maxMsPerTick;
        this.tasks = new LinkedList<>();
    }

    @Override
    public void run() {
        if (tasks.isEmpty()) return;

        long start = System.nanoTime();
        long maxNanos = (long) (maxMsPerTick * 1e6);

        while (!tasks.isEmpty() && System.nanoTime() - start < maxNanos) {
            Objects.requireNonNull(tasks.poll()).run();
        }
    }

    /**
     * Adds a task to the work load.
     * @param task the task
     */
    public void addTask(Runnable task) {
        tasks.add(task);
    }
}
