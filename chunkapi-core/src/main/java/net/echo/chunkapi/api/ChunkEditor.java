package net.echo.chunkapi.api;

import org.bukkit.Material;

public interface ChunkEditor<C, D> {

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

    void setBlockUsingNMS(int x, int y, int z, int combinedId);

    void setBlockOnChunk(C chunk, int x, int y, int z, D data);
}