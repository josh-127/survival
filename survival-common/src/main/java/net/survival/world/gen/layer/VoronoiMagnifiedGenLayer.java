package net.survival.world.gen.layer;

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

        for (int z = 0; z < source.lengthZ; ++z) {
            for (int x = 0; x < source.lengthX; ++x) {
                int pointIndex = x + z * source.lengthX;
                int indexX = pointIndex * 2;
                int indexZ = indexX + 1;
                random = rngFromPosition(random, offsetX + x, offsetZ + z);
                pointOffsets[indexX] = random.nextInt(cellSize) - (cellSize / 2);
                pointOffsets[indexZ] = random.nextInt(cellSize) - (cellSize / 2);
            }
        }

        for (int z = 0; z < lengthZ; ++z) {
            int sourceZ = z / cellSize;
            int pointIndexZ = sourceZ * source.lengthX;

            for (int x = 0; x < lengthX; ++x) {
                int sourceX = x / cellSize;
                int pointIndexX = sourceX;

                int pointIndexTL = pointIndexX + pointIndexZ;
                int pointIndexTR = pointIndexTL + 1;
                int pointIndexBL = pointIndexTL + source.lengthX;
                int pointIndexBR = pointIndexBL + 1;

                int baseL = (x / cellSize) * cellSize;
                int baseT = (z / cellSize) * cellSize;
                int baseR = ((x / cellSize) + 1) * cellSize;
                int baseB = ((z / cellSize) + 1) * cellSize;

                int pointTLX = baseL + pointOffsets[pointIndexTL * 2];
                int pointTLZ = baseT + pointOffsets[pointIndexTL * 2 + 1];
                int pointTRX = baseR + pointOffsets[pointIndexTR * 2];
                int pointTRZ = baseT + pointOffsets[pointIndexTR * 2 + 1];
                int pointBLX = baseL + pointOffsets[pointIndexBL * 2];
                int pointBLZ = baseB + pointOffsets[pointIndexBL * 2 + 1];
                int pointBRX = baseR + pointOffsets[pointIndexBR * 2];
                int pointBRZ = baseB + pointOffsets[pointIndexBR * 2 + 1];

                int manhattanDistTL = Math.abs(pointTLX - x) + Math.abs(pointTLZ - z);
                int manhattanDistTR = Math.abs(pointTRX - x) + Math.abs(pointTRZ - z);
                int manhattanDistBL = Math.abs(pointBLX - x) + Math.abs(pointBLZ - z);
                int manhattanDistBR = Math.abs(pointBRX - x) + Math.abs(pointBRZ - z);

                // TODO: Rewrite this terrible ugly code.
                int minDist = manhattanDistTL;
                int chosenSampleX = sourceX;
                int chosenSampleZ = sourceZ;
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