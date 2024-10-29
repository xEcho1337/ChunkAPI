package net.echo.chunkapi.editor;

import net.echo.chunkapi.ChunkAPI;
import net.echo.chunkapi.api.ChunkEditor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("all") // Intellij Idea STFU
public class ChunkEditorImpl implements ChunkEditor<LevelChunk, BlockState> {

    private final ChunkAPI chunkAPI;
    private final World world;

    public ChunkEditorImpl(ChunkAPI chunkAPI, World world) {
        this.chunkAPI = chunkAPI;
        this.world = world;
    }

    @Override
    public ChunkAPI getChunkAPI() {
        return chunkAPI;
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
        ServerLevel nmsWorld = craftWorld.getHandle();

        ChunkSource chunkProvider = nmsWorld.chunkSource;
        LevelChunk nmsChunk = chunkProvider.getChunkNow(x, z);

        // If the chunk doesn't exist because no one is loading it, create it
        if (nmsChunk == null) {
            nmsChunk = new LevelChunk(nmsWorld, new ChunkPos(x >> 4, z >> 4));
            nmsWorld.chunkSource.addLoadedChunk(nmsChunk);
        }

        BlockState data = Block.stateById(combinedId);
        setBlockOnChunk(nmsChunk, x, y, z, data);
    }

    @Override
    public void setBlockOnChunk(LevelChunk chunk, int x, int y, int z, BlockState data) {
        LevelChunkSection section = chunk.getSections()[y >> 4];

        if (section == null) {
            section = new LevelChunkSection(chunk.biomeRegistry);
            chunk.getSections()[y >> 4] = section;
        }

        section.setBlockState(x & 15, y & 15, z & 15, data); // 14M blocks/s
    }
}
