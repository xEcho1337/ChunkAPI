package net.echo.chunkapi.schematic;

import net.echo.common.AbstractChunkAPI;
import net.echo.common.api.ChunkEditor;
import net.echo.common.schematic.Schematic;
import net.echo.common.schematic.SchematicLoader;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class SchematicLoaderV1_21 implements SchematicLoader {

    private final AbstractChunkAPI chunkAPI;
    private Schematic schematic;

    public SchematicLoaderV1_21(AbstractChunkAPI chunkAPI) {
        this.chunkAPI = chunkAPI;
    }

    public void load(File file) throws IOException {
        this.schematic = new SchematicV1_21(file);
    }

    @Override
    public AbstractChunkAPI getAPI() {
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
