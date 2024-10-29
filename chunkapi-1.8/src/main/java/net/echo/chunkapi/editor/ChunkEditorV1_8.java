package net.echo.chunkapi.editor;

import net.echo.chunkapi.ChunkAPI;
import net.echo.chunkapi.api.ChunkEditor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;

@SuppressWarnings("all")
public class ChunkEditorV1_8 implements ChunkEditor<Chunk, IBlockData> {

    private final ChunkAPI chunkAPI;
    private final World world;

    public ChunkEditorV1_8(ChunkAPI chunkAPI, World world) {
        this.chunkAPI = chunkAPI;
        this.world = world;
    }

    @Override
    public void setBlock(int x, int y, int z, Material material) {
        setBlock(x, y, z, material.getId() << 4);
    }

    @Override
    public void setBlock(int x, int y, int z, int combinedId) {
        if (combinedId == 0) return; // Ignore air, useless

        chunkAPI.getChunkWorkload().addTask(() -> setBlockUsingNMS(x, y, z, combinedId));
    }

    @Override
    public void setAir(int x, int y, int z) {
        chunkAPI.getChunkWorkload().addTask(() -> setBlockUsingNMS(x, y, z, 0));
    }

    @Override
    public void setBlockUsingNMS(int x, int y, int z, int combinedId) {
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer nmsWorld = craftWorld.getHandle();

        long chunkId = LongHash.toLong(x >> 4, z >> 4);

        ChunkProviderServer chunkProvider = nmsWorld.chunkProviderServer;
        Chunk nmsChunk = chunkProvider.chunks.get(chunkId);

        if (nmsChunk == null) {
            nmsChunk = new Chunk(nmsWorld, x >> 4, z >> 4);
            chunkProvider.chunks.put(chunkId, nmsChunk);
        }

        IBlockData data = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combinedId);
        setBlockOnChunk(nmsChunk, x, y, z, data);
    }

    @Override
    public void setBlockOnChunk(Chunk chunk, int x, int y, int z, IBlockData data) {
        ChunkSection section = chunk.getSections()[y >> 4];

        if (section == null) {
            section = new ChunkSection(y >> 4 << 4, !chunk.world.worldProvider.o());
            chunk.getSections()[y >> 4] = section;
        }

        section.setType(x & 15, y & 15, z & 15, data); // 14M blocks/s
    }
}
