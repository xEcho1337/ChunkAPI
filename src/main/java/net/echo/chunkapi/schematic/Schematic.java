package net.echo.chunkapi.schematic;

import de.tr7zw.changeme.nbtapi.NBTFile;

import java.io.File;
import java.io.IOException;

public class Schematic {

    private int[] blocks;
    private int width; // X-axis
    private int height; // Y-axis
    private int length; // Z-axis

    public Schematic(File file) throws IOException {
        load(file);
    }

    private int coordinatesToIndex(int x, int y, int z) {
        return (y * length + z) * width + x;
    }

    private void load(File file) throws IOException {
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

    /**
     * Gets a block from the given position.
     *
     * @param x should never be bigger than <code>getWidth() - 1</code>
     * @param y should never be bigger than <code>getHeight() - 1</code>
     * @param z should never be bigger than <code>getLength() - 1</code>
     * @return the combined id of the block, encoded as <code>(blockId << 4 | data)</code>
     */
    public int getBlockAt(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= length) {
            throw new IllegalArgumentException("Coordinates (" + x + ", " + y + ", " + z + ") are out of bounds for " +
                    "(" + width + ", " + height + ", " + length + ")");
        }

        return blocks[coordinatesToIndex(x, y, z)];
    }

    /**
     * Gets the block list of the schematic.
     */
    public int[] getBlocks() {
        return blocks;
    }

    /**
     * Returns the X-axis width of the schematic.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the Y-axis height of the schematic.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the Z-axis length of the schematic.
     */
    public int getLength() {
        return length;
    }
}
