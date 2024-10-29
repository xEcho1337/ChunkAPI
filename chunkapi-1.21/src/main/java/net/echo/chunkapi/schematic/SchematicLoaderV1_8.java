package net.echo.chunkapi.schematic;

import net.echo.chunkapi.ChunkAPI;
import net.echo.chunkapi.api.ChunkEditor;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class SchematicLoaderV1_8 implements SchematicLoader {

    private final ChunkAPI chunkAPI;
    private Schematic schematic;

    public SchematicLoaderV1_8(ChunkAPI chunkAPI) {
        this.chunkAPI = chunkAPI;
    }

    public void load(File file) throws IOException {
        this.schematic = new SchematicV1_8(file);
    }

    @Override
    public ChunkEditor<?, ?> getChunkEditor(World world) {
        return chunkAPI.getChunkEditor(world);
    }

    @Override
    public Schematic getSchematic() {
        return schematic;
    }

}
