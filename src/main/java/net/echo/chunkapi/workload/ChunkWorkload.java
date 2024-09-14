package net.echo.chunkapi.workload;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

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

        int initialSize = tasks.size();

        while (!tasks.isEmpty() && System.nanoTime() - start < maxNanos) {
            Objects.requireNonNull(tasks.poll()).run();
        }

        int finalSize = tasks.size();

        System.out.println("Updated " + (initialSize - finalSize) + " chunks in " + (System.nanoTime() - start) / 1e6 + "ms");
    }

    /**
     * Adds a task to the work load.
     * @param task the task
     */
    public void addTask(Runnable task) {
        tasks.add(task);
    }
}
