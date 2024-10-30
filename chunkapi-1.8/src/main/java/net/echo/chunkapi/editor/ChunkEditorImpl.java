package net.echo.chunkapi.editor;

import net.echo.common.AbstractChunkAPI;
import net.echo.common.api.ChunkEditor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("all") // Intellij Idea STFU
public class ChunkEditorImpl implements ChunkEditor<Chunk, IBlockData> {

    private final AbstractChunkAPI chunkAPI;
    private final World world;

    public ChunkEditorImpl(AbstractChunkAPI chunkAPI, World world) {
        this.chunkAPI = chunkAPI;
        this.world = world;
    }

    @Override
    public AbstractChunkAPI getChunkAPI() {
        return chunkAPI;
    }

    @Override
    public CompletableFuture<Boolean> setBlock(int x, int y, int z, Material material) {
        return setBlock(x, y, z, material.getId() << 4);
    }

    @Override
    public CompletableFuture<Boolean> setBlock(int x, int y, int z, int combinedId) {
        CompletableFuture<Boolean> callback = new CompletableFuture<>();

        IBlockData data = getBlockAt(x, y, z);

        if (data.getBlock().getMaterial() == net.minecraft.server.v1_8_R3.Material.AIR && combinedId == 0) {
            callback.complete(false);
            return callback;
        }

        chunkAPI.getChunkWorkload().addTask(() -> {
            setBlockUsingNMS(x, y, z, combinedId);
            callback.complete(true);
        });

        return callback;
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

        setBlockOnChunk(nmsChunk, x, y, z, Block.getByCombinedId(combinedId));
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

    private IBlockData getBlockAt(int x, int y, int z) {
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer nmsWorld = craftWorld.getHandle();

        long chunkId = LongHash.toLong(x >> 4, z >> 4);

        ChunkProviderServer chunkProvider = nmsWorld.chunkProviderServer;
        Chunk nmsChunk = chunkProvider.chunks.get(chunkId);

        if (nmsChunk == null) {
            nmsChunk = new Chunk(nmsWorld, x >> 4, z >> 4);
            return null;
        }

        ChunkSection section = nmsChunk.getSections()[y >> 4];

        if (section == null) {
            return null;
        }

        return section.getType(x & 15, y & 15, z & 15);
    }
}
