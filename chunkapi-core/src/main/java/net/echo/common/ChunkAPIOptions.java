package net.echo.common;

public class ChunkAPIOptions {

    private int maxMsPerTick = 20;

    /**
     * Returns the maximum milliseconds the workload can last each tick.
     */
    public int getMaxMsPerTick() {
        return maxMsPerTick;
    }

    /**
     * Specifies the maximum milliseconds the workload can last each tick.
     * @param maxMsPerTick the maximum milliseconds
     */
    public ChunkAPIOptions setMaxMsPerTick(int maxMsPerTick) {
        this.maxMsPerTick = maxMsPerTick;
        return this;
    }
}
