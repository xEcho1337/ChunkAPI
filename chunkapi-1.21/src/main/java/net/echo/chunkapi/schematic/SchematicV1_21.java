package net.echo.chunkapi.schematic;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTFile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

        this.width = schematic.getInteger("Width");
        this.height = schematic.getInteger("Height");
        this.length = schematic.getInteger("Length");

        this.blocks = new int[width * height * length];

        NBTCompound blocks = schematic.getCompound("Blocks");

        if (blocks == null) {
            throw new IOException("Invalid schematic! No blocks found!");
        }

        NBTCompound palette = blocks.getCompound("Palette");

        if (palette == null) {
            throw new IOException("Invalid schematic! No palette found!");
        }

        Map<Integer, Material> paletteMap = new HashMap<>();

        for (String key : palette.getKeys()) {
            System.out.println(key);

            NamespacedKey namespace = NamespacedKey.fromString(key);

            System.out.println(namespace);

            Material material = Registry.MATERIAL.get(Objects.requireNonNull(namespace));

            paletteMap.put(palette.getInteger(key), material);
        }

        byte[] data = nbtFile.getByteArray("Data");

        if (data == null) {
            throw new IOException("Invalid schematic! No data found!");
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int i = coordinatesToIndex(x, y, z);

                    int block = data[i];
                    Material converted = paletteMap.get(block);

                    assert this.blocks != null;
                    this.blocks[i] = converted.getId();
                }
            }
        }
    }

    public BlockData stringToBlockData(String input) {
        try {
            String[] parts = input.split("\\[", 2);
            String type = parts[0].replace("minecraft:", "").toUpperCase();

            Material material = Material.valueOf(type);
            BlockData blockData = Blocks.AIR.defaultBlockState().createCraftBlockData();

            if (parts.length > 1) {
                String propertiesString = parts[1].replace("]", "");
                String[] properties = propertiesString.split(",");

                for (String property : properties) {
                    String[] keyValue = property.split("=");
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    // blockData.getAsString("[" + key + "=" + value + "]");
                }
            }

            return blockData;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
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
