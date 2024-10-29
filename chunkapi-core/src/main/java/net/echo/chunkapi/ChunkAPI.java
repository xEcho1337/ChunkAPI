package net.echo.chunkapi;

import net.echo.chunkapi.api.ChunkEditor;
import net.echo.chunkapi.schematic.SchematicLoader;
import net.echo.chunkapi.workload.ChunkWorkload;
import org.bukkit.World;

public interface ChunkAPI {

    /**
     * Returns a chunk editor for the given world.
     * @param world the world
     */
    ChunkEditor<?, ?> getChunkEditor(World world);

    /**
     * Returns the chunk workload.
     */
    ChunkWorkload getChunkWorkload();

    /**
     * Returns the schematic loader.
     */
    SchematicLoader getSchematicLoader();
}
