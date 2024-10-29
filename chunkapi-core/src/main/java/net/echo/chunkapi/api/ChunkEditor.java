package net.echo.chunkapi.api;

import net.echo.chunkapi.ChunkAPI;
import org.bukkit.Material;

import java.util.concurrent.CompletableFuture;

public interface ChunkEditor<C, D> {

    /**
     * Get the {@link ChunkAPI} instance
     * @return the {@link ChunkAPI}
     */
    ChunkAPI getChunkAPI();

    /**
     * Updates a block at a given position to a given type.
     * This method ignores AIR blocks for performance.
     *
     * @param x the block x position
     * @param y the block y position
     * @param z the block z position
     * @param material the block material
     */
    void setBlock(int x, int y, int z, Material material);

    /**
     * Updates a block at a given position to the given type.
     * This method ignores AIR blocks for performance.
     *
     * @param x the block x position
     * @param y the block y position
     * @param z the block z position
     * @param combinedId the combined id of the block
     */
    void setBlock(int x, int y, int z, int combinedId);

    /**
     * Updates a block at a given position to air.
     *
     * @param x the block x position
     * @param y the block y position
     * @param z the block z position
     */
    void setAir(int x, int y, int z);

    /**
     * Sets the block using Minecraft internals
     * @param x the block x position
     * @param y the block y position
     * @param z the block z position
     * @param combinedId the combined id of the block
     */
    void setBlockUsingNMS(int x, int y, int z, int combinedId);

    /**
     * Sets the block on a specific chunk using Minecraft internals
     * @param chunk the chunk in this event
     * @param x the block x position
     * @param y the block y position
     * @param z the block z position
     * @param data the data of the block
     */
    void setBlockOnChunk(C chunk, int x, int y, int z, D data);

    /**
     * Fills an area with a given material.
     * @return a future that completes when the area is filled
     */
    default CompletableFuture<Void> fillArea(int x, int y, int z, int endX, int endY, int endZ, Material material) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        for (int i = x; i <= endX; i++) {
            for (int j = y; j <= endY; j++) {
                for (int k = z; k <= endZ; k++) {
                    setBlock(i, j, k, material);
                }
            }
        }

        getChunkAPI().getChunkWorkload().addTask(() -> future.complete(null));

        return future;
    }
}