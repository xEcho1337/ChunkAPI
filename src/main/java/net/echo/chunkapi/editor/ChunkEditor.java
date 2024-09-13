package net.echo.chunkapi.modifier;

import net.echo.chunkapi.ChunkAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;

public class ChunkModifier {

    private final ChunkAPI chunkAPI;
    private final World world;

    public ChunkModifier(ChunkAPI chunkAPI, World world) {
        this.chunkAPI = chunkAPI;
        this.world = world;
    }

    /**
     * Deletes a chunk at his x and z coordinates. To convert a block coordinate to chunk coordinates do:
     * <code>x >> 4</code> for the x and <code>z >> 4</code> for the z.
     *
     * @param x the chunk x coordinate
     * @param z the chunk z coordinate
     */
    public void deleteChunk(int x, int z) {
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer nmsWorld = craftWorld.getHandle();

        ChunkProviderServer provider = nmsWorld.chunkProviderServer;
        provider.chunks.remove(LongHash.toLong(x, z));
    }

    /**
     * Updates a block at a given position to a given type.
     *
     * @param x the block x position
     * @param y the block y position
     * @param z the block z position
     * @param block the block state
     */
    public void setBlock(int x, int y, int z, Block block) {
        chunkAPI.getChunkWorkload().addTask(() -> setBlockUsingNMS(x, y, z, block));
    }

    private void setBlockUsingNMS(int x, int y, int z, Block block) {
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer nmsWorld = craftWorld.getHandle();

        Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
        net.minecraft.server.v1_8_R3.Block craftBlock = (net.minecraft.server.v1_8_R3.Block) block;

        IBlockData data = craftBlock.getBlockData();

        nmsChunk.a(new BlockPosition(x & 15, y & 15, z & 15), data);
    }
}
