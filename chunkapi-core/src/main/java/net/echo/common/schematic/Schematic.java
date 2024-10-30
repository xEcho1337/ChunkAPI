package net.echo.common.schematic;

import java.io.File;
import java.io.IOException;

public interface Schematic {

    private int coordinatesToIndex(int x, int y, int z) {
        return (y * getLength() + z) * getWidth() + x;
    }

   void load(File file) throws IOException;

    /**
     * Gets a block from the given position.
     *
     * @param x should never be bigger than <code>getWidth() - 1</code>
     * @param y should never be bigger than <code>getHeight() - 1</code>
     * @param z should never be bigger than <code>getLength() - 1</code>
     * @return the combined id of the block, encoded as <code>(blockId << 4 | data)</code>
     */
    int getBlockAt(int x, int y, int z);

    /**
     * Gets the block list of the schematic.
     */
    int[] getBlocks();

    /**
     * Returns the X-axis width of the schematic.
     */
    int getWidth();

    /**
     * Returns the Y-axis height of the schematic.
     */
    int getHeight();

    /**
     * Returns the Z-axis length of the schematic.
     */
    int getLength();
}
