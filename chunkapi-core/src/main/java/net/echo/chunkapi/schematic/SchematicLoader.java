package net.echo.chunkapi.schematic;

import net.echo.chunkapi.api.ChunkEditor;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public interface SchematicLoader {

    void load(File file) throws IOException;

    default void print(World world, int baseX, int baseY, int baseZ) {
        ChunkEditor<?, ?> editor = getChunkEditor(world);
        Schematic schematic = getSchematic();

        for (int x = 0; x < schematic.getWidth(); x++) {
            for (int y = 0; y < schematic.getHeight(); y++) {
                for (int z = 0; z < schematic.getLength(); z++) {
                    int block = schematic.getBlockAt(x, y, z);

                    int x1 = baseX + x;
                    int y1 = baseY + y;
                    int z1 = baseZ + z;

                    editor.setBlock(x1, y1, z1, block);
                }
            }
        }
    }

    ChunkEditor<?, ?> getChunkEditor(World world);

    Schematic getSchematic();
}
