package net.echo.chunkapi.editor;

import net.echo.common.AbstractChunkAPI;
import net.echo.common.api.ChunkEditor;
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

        BlockState data = getBlockAt(x, y, z);

        if (data.getBukkitMaterial() == Material.AIR && combinedId == 0) {
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
        ServerLevel nmsWorld = craftWorld.getHandle();

        ChunkSource chunkProvider = nmsWorld.chunkSource;
        LevelChunk nmsChunk = chunkProvider.getChunkNow(x, z);

        if (nmsChunk == null) {
            nmsChunk = new LevelChunk(nmsWorld, new ChunkPos(x >> 4, z >> 4));
            nmsWorld.chunkSource.moonrise$setFullChunk(x >> 4, z >> 4, nmsChunk);
        }

        setBlockOnChunk(nmsChunk, x, y, z, Block.stateById(combinedId));
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

    private BlockState getBlockAt(int x, int y, int z) {
        CraftWorld craftWorld = (CraftWorld) world;
        ServerLevel nmsWorld = craftWorld.getHandle();

        ChunkSource chunkProvider = nmsWorld.chunkSource;
        LevelChunk chunk = chunkProvider.getChunkNow(x, z);

        if (chunk == null) {
            chunk = new LevelChunk(nmsWorld, new ChunkPos(x >> 4, z >> 4));
            return null;
        }

        LevelChunkSection section = chunk.getSections()[y >> 4];

        if (section == null) {
            section = new LevelChunkSection(chunk.biomeRegistry);
            chunk.getSections()[y >> 4] = section;
        }

        return section.getBlockState(x & 15, y & 15, z & 15);
    }
}
