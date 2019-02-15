package net.survival.client.graphics;

import net.survival.block.Column;
import net.survival.blocktype.BlockFace;

class ColumnDisplay implements GraphicsResource
{
    public final int cx;
    public final int cz;
    private final ColumnFaceDisplay nonCubicDisplay;
    private final ColumnFaceDisplay topFaceDisplay;
    private final ColumnFaceDisplay bottomFaceDisplay;
    private final ColumnFaceDisplay leftFaceDisplay;
    private final ColumnFaceDisplay rightFaceDisplay;
    private final ColumnFaceDisplay frontFaceDisplay;
    private final ColumnFaceDisplay backFaceDisplay;

    private ColumnDisplay(
            int cx, int cz,
            ColumnFaceDisplay nonCubicDisplay,
            ColumnFaceDisplay topFaceDisplay,
            ColumnFaceDisplay bottomFaceDisplay,
            ColumnFaceDisplay leftFaceDisplay,
            ColumnFaceDisplay rightFaceDisplay,
            ColumnFaceDisplay frontFaceDisplay,
            ColumnFaceDisplay backFaceDisplay)
    {
        this.cx = cx;
        this.cz = cz;
        this.nonCubicDisplay = nonCubicDisplay;
        this.topFaceDisplay = topFaceDisplay;
        this.bottomFaceDisplay = bottomFaceDisplay;
        this.leftFaceDisplay = leftFaceDisplay;
        this.rightFaceDisplay = rightFaceDisplay;
        this.frontFaceDisplay = frontFaceDisplay;
        this.backFaceDisplay = backFaceDisplay;
    }

    public static ColumnDisplay create(
            int cx, int cz,
            Column column,
            Column leftAdjacentColumn,
            Column rightAdjacentColumn,
            Column frontAdjacentColumn,
            Column backAdjacentColumn)
    {
        var nonCubicDisplay = ColumnFaceDisplay.create(cx, cz, column, null, null);
        var topFaceDisplay = ColumnFaceDisplay.create(cx, cz, column, null, BlockFace.TOP);
        var bottomFaceDisplay = ColumnFaceDisplay.create(cx, cz, column, null, BlockFace.BOTTOM);
        var leftFaceDisplay = ColumnFaceDisplay.create(cx, cz, column, leftAdjacentColumn, BlockFace.LEFT);
        var rightFaceDisplay = ColumnFaceDisplay.create(cx, cz, column, rightAdjacentColumn, BlockFace.RIGHT);
        var frontFaceDisplay = ColumnFaceDisplay.create(cx, cz, column, frontAdjacentColumn, BlockFace.FRONT);
        var backFaceDisplay = ColumnFaceDisplay.create(cx, cz, column, backAdjacentColumn, BlockFace.BACK);

        return new ColumnDisplay(
                cx, cz,
                nonCubicDisplay,
                topFaceDisplay,
                bottomFaceDisplay,
                leftFaceDisplay,
                rightFaceDisplay,
                frontFaceDisplay,
                backFaceDisplay);
    }
    
    @Override
    public void close() throws RuntimeException {
        nonCubicDisplay.close();
        topFaceDisplay.close();
        bottomFaceDisplay.close();
        leftFaceDisplay.close();
        rightFaceDisplay.close();
        frontFaceDisplay.close();
        backFaceDisplay.close();
    }

    public void display() {
        nonCubicDisplay.displayBlocks();
        topFaceDisplay.displayBlocks();
        bottomFaceDisplay.displayBlocks();
        leftFaceDisplay.displayBlocks();
        rightFaceDisplay.displayBlocks();
        frontFaceDisplay.displayBlocks();
        backFaceDisplay.displayBlocks();
    }
}