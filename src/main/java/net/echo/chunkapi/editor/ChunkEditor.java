package net.echo.chunkapi.editor;

import net.echo.chunkapi.ChunkAPI;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.Chunk;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;

public class ChunkEditor {

    private final ChunkAPI chunkAPI;
    private final World world;

    public ChunkEditor(ChunkAPI chunkAPI, World world) {
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
     * @param material the block material
     */
    public void setBlock(int x, int y, int z, Material material) {
        setBlock(x, y, z, material.getId() << 4);
    }

    public void setBlock(int x, int y, int z, int combinedId) {
        chunkAPI.getChunkWorkload().addTask(() -> setBlockUsingNMS(x, y, z, combinedId));
    }

    // TODO: do not set block if the chunk is a new generated chunk, as by default it will be all air
    private void setBlockUsingNMS(int x, int y, int z, int combinedId) {
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer nmsWorld = craftWorld.getHandle();

        long chunkId = LongHash.toLong(x >> 4, z >> 4);

        ChunkProviderServer chunkProvider = nmsWorld.chunkProviderServer;
        Chunk nmsChunk = chunkProvider.chunks.get(chunkId);

        // If the chunk doesn't exist because no one is loading it, create it
        if (nmsChunk == null) {
            nmsChunk = new Chunk(nmsWorld, x >> 4, z >> 4);
            chunkProvider.chunks.put(chunkId, nmsChunk);
        }

        IBlockData data = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combinedId);
        setBlockOnChunk(nmsChunk, x, y, z, data);
    }

    private void setBlockOnChunk(Chunk chunk, int x, int y, int z, IBlockData data) {
        ChunkSection section = chunk.getSections()[y >> 4];

        if (section == null) {
            section = new ChunkSection(y >> 4 << 4, !chunk.world.worldProvider.o());
            chunk.getSections()[y >> 4] = section;
        }

        int blockX = x & 15;
        int blockY = y & 15;
        int blockZ = z & 15;

        section.setType(blockX, blockY, blockZ, data); // 14M blocks/s
        // section.getIdArray()[blockY << 8 | blockZ << 4 | blockX] = (char) Block.d.b(data); // 18.5M blocks/s
    }
}
