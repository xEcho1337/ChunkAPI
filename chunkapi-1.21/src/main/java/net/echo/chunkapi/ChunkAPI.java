package net.echo.chunkapi;

import de.tr7zw.changeme.nbtapi.NBT;
import net.echo.common.AbstractChunkAPI;
import net.echo.common.ChunkAPIOptions;
import net.echo.common.api.ChunkEditor;
import net.echo.chunkapi.editor.ChunkEditorImpl;
import net.echo.common.schematic.SchematicLoader;
import net.echo.chunkapi.schematic.SchematicLoaderV1_21;
import net.echo.common.workload.ChunkWorkload;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkAPI implements AbstractChunkAPI {

    private final ChunkWorkload chunkWorkload;
    private final SchematicLoader schematicLoader;

    private ChunkAPI(JavaPlugin plugin, ChunkAPIOptions options) {
        if (!NBT.preloadApi()) {
            plugin.getLogger().warning("NBT-API wasn't initialized properly!");
        }

        this.schematicLoader = new SchematicLoaderV1_21(this);
        this.chunkWorkload = new ChunkWorkload(options.getMaxMsPerTick());

        Bukkit.getScheduler().runTaskTimer(plugin, chunkWorkload, 0, options.getMaxMsPerTick());
    }

    public static AbstractChunkAPI create(JavaPlugin plugin) {
        return new ChunkAPI(plugin, new ChunkAPIOptions());
    }

    public static AbstractChunkAPI create(JavaPlugin plugin, ChunkAPIOptions options) {
        return new ChunkAPI(plugin, options);
    }

    public ChunkEditor<?, ?> getChunkEditor(World world) {
        return new ChunkEditorImpl(this, world);
    }

    public ChunkWorkload getChunkWorkload() {
        return chunkWorkload;
    }

    public SchematicLoader getSchematicLoader() {
        return schematicLoader;
    }
}
