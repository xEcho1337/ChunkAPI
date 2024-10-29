package net.echo.chunkapi.schematic;

import de.tr7zw.changeme.nbtapi.NBTFile;

import java.io.File;
import java.io.IOException;

public class SchematicV1_8 implements Schematic {

    private int[] blocks;
    private int width; // X-axis
    private int height; // Y-axis
    private int length; // Z-axis

    public SchematicV1_8(File file) throws IOException {
        load(file);
    }

    private int coordinatesToIndex(int x, int y, int z) {
        return (y * length + z) * width + x;
    }

    public void load(File file) throws IOException {
        NBTFile nbtFile = new NBTFile(file);

        this.width = nbtFile.getInteger("Width");
        this.height = nbtFile.getInteger("Height");
        this.length = nbtFile.getInteger("Length");

        this.blocks = new int[width * height * length];

        byte[] blocks = nbtFile.getByteArray("Blocks");
        byte[] additionalData = nbtFile.getByteArray("Data");

        if (blocks == null || additionalData == null) {
            throw new IOException("Invalid schematic! No blocks or data found!");
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int i = coordinatesToIndex(x, y, z);

                    byte block = blocks[i];
                    byte data = additionalData[i];

                    this.blocks[i] = (data << 12) | block;
                }
            }
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
