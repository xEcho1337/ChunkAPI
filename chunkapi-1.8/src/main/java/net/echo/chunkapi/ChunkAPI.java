package net.echo.chunkapi;

import de.tr7zw.changeme.nbtapi.NBT;
import net.echo.chunkapi.api.ChunkEditor;
import net.echo.chunkapi.editor.ChunkEditorImpl;
import net.echo.chunkapi.schematic.SchematicLoader;
import net.echo.chunkapi.schematic.SchematicLoaderV1_8;
import net.echo.chunkapi.workload.ChunkWorkload;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkAPI {

    private final ChunkWorkload chunkWorkload;
    private final SchematicLoader schematicLoader;

    private ChunkAPI(JavaPlugin plugin, ChunkAPIOptions options) {
        if (!NBT.preloadApi()) {
            plugin.getLogger().warning("NBT-API wasn't initialized properly!");
        }

        this.schematicLoader = new SchematicLoaderV1_8(this);
        this.chunkWorkload = new ChunkWorkload(options.getMaxMsPerTick());

        Bukkit.getScheduler().runTaskTimer(plugin, chunkWorkload, 0, options.getMaxMsPerTick());
    }

    /**
     * Creates an instance of the API.
     * @param plugin the parent plugin
     */
    public static ChunkAPI create(JavaPlugin plugin) {
        return new ChunkAPI(plugin, new ChunkAPIOptions());
    }

    /**
     * Creates an instance of the API.
     * @param plugin the parent plugin
     * @param options the options of the API
     */
    public static ChunkAPI create(JavaPlugin plugin, ChunkAPIOptions options) {
        return new ChunkAPI(plugin, options);
    }

    /**
     * Returns a chunk editor for the given world.
     * @param world the world
     */
    public ChunkEditor<?, ?> getChunkEditor(World world) {
        return new ChunkEditorImpl(this, world);
    }

    /**
     * Returns the chunk workload.
     */
    public ChunkWorkload getChunkWorkload() {
        return chunkWorkload;
    }

    /**
     * Returns the schematic loader.
     */
    public SchematicLoader getSchematicLoader() {
        return schematicLoader;
    }
}
