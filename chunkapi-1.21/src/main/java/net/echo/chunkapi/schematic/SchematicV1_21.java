package net.echo.chunkapi.schematic;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTFile;
import io.papermc.paper.plugin.entrypoint.strategy.modern.ModernPluginLoadingStrategy;
import net.echo.common.schematic.Schematic;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftLegacy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchematicV1_21 implements Schematic {

    private int[] blocks;
    private int width; // X-axis
    private int height; // Y-axis
    private int length; // Z-axis

    public SchematicV1_21(File file) throws IOException {
        load(file);
    }

    private int coordinatesToIndex(int x, int y, int z) {
        return x + z * width + y * width * length;
    }

    public void load(File file) throws IOException {
        NBTFile nbtFile = new NBTFile(file);

        NBTCompound schematic = nbtFile.getCompound("Schematic");

        this.width = schematic.getShort("Width");
        this.height = schematic.getShort("Height");
        this.length = schematic.getShort("Length");

        this.blocks = new int[width * height * length];

        NBTCompound blocks = schematic.getCompound("Blocks");

        if (blocks == null) {
            throw new IOException("Invalid schematic! No blocks found!");
        }

        NBTCompound palette = blocks.getCompound("Palette");

        if (palette == null) {
            throw new IOException("Invalid schematic! No palette found!");
        }

        Map<Integer, Integer> paletteMap = new HashMap<>();

        for (String key : palette.getKeys()) {
            paletteMap.put(palette.getInteger(key), stringToBlockData(key));
        }

        byte[] data = blocks.getByteArray("Data");

        if (data == null) {
            throw new IOException("Invalid schematic! No data found!");
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int i = coordinatesToIndex(x, y, z);

                    int block = data[i];

                    assert this.blocks != null;
                   // System.out.println(i + " -> Block: " + block + " | From palette: " + paletteMap.get(block));
                    this.blocks[i] = paletteMap.get(block);
                }
            }
        }
    }

    public int stringToBlockData(String input) {
        try {
            String[] parts = input.split("\\[");

            if (parts.length != 2) {
                System.out.println(input + " -> " + input.replace("minecraft:", "").toUpperCase());
                Material material = Material.getMaterial(input.replace("minecraft:", "").toUpperCase());
                Block block = CraftBlockType.bukkitToMinecraft(material);

                if (block == null) {
                    System.out.println(input + " IS NULL!");
                    return -1;
                }
                BlockType blockType = CraftBlockType.minecraftToBukkitNew(block);
                BlockData blockData = CraftBlockData.newData(blockType, null);

                System.out.println(input + " -> " + blockData.getMaterial().getId());
                return blockData.getMaterial().getId();
            }

            String type = parts[0].replace("minecraft:", "").toUpperCase();

            Material material = Material.getMaterial(type);
            Block block = CraftBlockType.bukkitToMinecraft(material);

            BlockType blockType = CraftBlockType.minecraftToBukkitNew(block);
            BlockData blockData = CraftBlockData.newData(blockType, "[" + parts[1]);

            System.out.println(input + " -> " + blockData.getMaterial().getId());
            return blockData.getMaterial().getId();
        } catch (IllegalArgumentException e) {
            e.printStackTrace(System.err);
            return -1;
        }
    }

    public int getBlockAt(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= length) {
            throw new IllegalArgumentException("Coordinates (" + x + ", " + y + ", " + z + ") are out of bounds for " +
                    "(" + width + ", " + height + ", " + length + ")");
        }

        return blocks[coordinatesToIndex(x, y, z)];
    }

    public int[] getBlocks() {
        return blocks;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }
}
