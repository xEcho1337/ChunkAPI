package net.echo.common;

import net.echo.common.api.ChunkEditor;
import net.echo.common.schematic.SchematicLoader;
import net.echo.common.workload.ChunkWorkload;
import org.bukkit.World;

public interface AbstractChunkAPI {

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
