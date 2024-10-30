package net.echo.chunkapi.schematic;

import net.echo.common.ChunkAPI;
import net.echo.common.api.ChunkEditor;
import net.echo.common.schematic.Schematic;
import net.echo.common.schematic.SchematicLoader;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class SchematicLoaderV1_20_5 implements SchematicLoader {

    private final ChunkAPI chunkAPI;
    private Schematic schematic;

    public SchematicLoaderV1_20_5(ChunkAPI chunkAPI) {
        this.chunkAPI = chunkAPI;
    }

    public void load(File file) throws IOException {
        this.schematic = new SchematicV1_20_5(file);
    }

    @Override
    public ChunkAPI getAPI() {
        return chunkAPI;
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
