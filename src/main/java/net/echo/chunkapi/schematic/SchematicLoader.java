package net.echo.chunkapi.schematic;

import net.echo.chunkapi.ChunkAPI;
import net.echo.chunkapi.editor.ChunkEditor;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class SchematicLoader {

    private final ChunkAPI chunkAPI;
    private Schematic schematic;

    public SchematicLoader(ChunkAPI chunkAPI) {
        this.chunkAPI = chunkAPI;
    }

    public void loadSchematic(File file) throws IOException {
        this.schematic = new Schematic(file);
    }

    public void startPrinting(World world, int baseX, int baseY, int baseZ) {
        ChunkEditor editor = chunkAPI.getChunkEditor(world);

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
}
