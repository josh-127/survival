package net.survival.gen.layer;

import java.util.Random;

class VoronoiMagnifiedGenLayer extends GenLayer
{
    private final GenLayer source;
    private final int cellSize;

    private final int[] pointOffsets;

    private Random random;

    public VoronoiMagnifiedGenLayer(GenLayer source, int scaleFactor, long seed) {
        super((source.lengthX - 2) * scaleFactor, (source.lengthZ - 2) * scaleFactor, seed);

        this.source = source;
        this.cellSize = scaleFactor;

        pointOffsets = new int[source.lengthX * source.lengthZ * 2];
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        source.generate(offsetX, offsetZ);

        for (var z = 0; z < source.lengthZ; ++z) {
            for (var x = 0; x < source.lengthX; ++x) {
                var pointIndex = x + z * source.lengthX;
                var indexX = pointIndex * 2;
                var indexZ = indexX + 1;
                random = rngFromPosition(random, offsetX + x, offsetZ + z);
                pointOffsets[indexX] = random.nextInt(cellSize) - (cellSize / 2);
                pointOffsets[indexZ] = random.nextInt(cellSize) - (cellSize / 2);
            }
        }

        for (var z = 0; z < lengthZ; ++z) {
            var sourceZ = z / cellSize;
            var pointIndexZ = sourceZ * source.lengthX;

            for (var x = 0; x < lengthX; ++x) {
                var sourceX = x / cellSize;
                var pointIndexX = sourceX;

                var pointIndexTL = pointIndexX + pointIndexZ;
                var pointIndexTR = pointIndexTL + 1;
                var pointIndexBL = pointIndexTL + source.lengthX;
                var pointIndexBR = pointIndexBL + 1;

                var baseL = (x / cellSize) * cellSize;
                var baseT = (z / cellSize) * cellSize;
                var baseR = ((x / cellSize) + 1) * cellSize;
                var baseB = ((z / cellSize) + 1) * cellSize;

                var pointTLX = baseL + pointOffsets[pointIndexTL * 2];
                var pointTLZ = baseT + pointOffsets[pointIndexTL * 2 + 1];
                var pointTRX = baseR + pointOffsets[pointIndexTR * 2];
                var pointTRZ = baseT + pointOffsets[pointIndexTR * 2 + 1];
                var pointBLX = baseL + pointOffsets[pointIndexBL * 2];
                var pointBLZ = baseB + pointOffsets[pointIndexBL * 2 + 1];
                var pointBRX = baseR + pointOffsets[pointIndexBR * 2];
                var pointBRZ = baseB + pointOffsets[pointIndexBR * 2 + 1];

                var manhattanDistTL = Math.abs(pointTLX - x) + Math.abs(pointTLZ - z);
                var manhattanDistTR = Math.abs(pointTRX - x) + Math.abs(pointTRZ - z);
                var manhattanDistBL = Math.abs(pointBLX - x) + Math.abs(pointBLZ - z);
                var manhattanDistBR = Math.abs(pointBRX - x) + Math.abs(pointBRZ - z);

                // TODO: Rewrite this terrible ugly code.
                var minDist = manhattanDistTL;
                var chosenSampleX = sourceX;
                var chosenSampleZ = sourceZ;
                if (manhattanDistTR < manhattanDistTL) {
                    minDist = manhattanDistTR;
                    chosenSampleX = sourceX + 1;
                    chosenSampleZ = sourceZ;
                }
                if (manhattanDistBL < minDist) {
                    minDist = manhattanDistBL;
                    chosenSampleX = sourceX;
                    chosenSampleZ = sourceZ + 1;
                }
                if (manhattanDistBR < minDist) {
                    minDist = manhattanDistBR;
                    chosenSampleX = sourceX + 1;
                    chosenSampleZ = sourceZ + 1;
                }

                map[x + z * lengthX] = source.sampleNearest(chosenSampleX, chosenSampleZ);
            }
        }
    }
}