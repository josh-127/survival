package net.survival.graphics;

import net.survival.block.Column;

class ColumnDisplay implements GraphicsResource
{
    public final int cx;
    public final int cz;
    private final ColumnFaceDisplay faceDisplay;

    private ColumnDisplay(int cx, int cz, ColumnFaceDisplay faceDisplay) {
        this.cx = cx;
        this.cz = cz;
        this.faceDisplay = faceDisplay;
    }

    public static ColumnDisplay create(
            int cx, int cz,
            Column column,
            Column leftAdjacentColumn,
            Column rightAdjacentColumn,
            Column frontAdjacentColumn,
            Column backAdjacentColumn)
    {
        var faceDisplay = ColumnFaceDisplay.create(
                cx,
                cz,
                column,
                leftAdjacentColumn,
                rightAdjacentColumn,
                frontAdjacentColumn,
                backAdjacentColumn);

        return new ColumnDisplay(
                cx, cz,
                faceDisplay);
    }
    
    @Override
    public void close() throws RuntimeException {
        faceDisplay.close();
    }

    public void displayBlocks() {
        faceDisplay.displayBlocks();
    }
}