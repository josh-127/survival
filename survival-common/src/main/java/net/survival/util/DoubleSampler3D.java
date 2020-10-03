package net.survival.util;

public interface DoubleSampler3D {
    double sampleNearest(int x, int y, int z);

    double sampleLinear(double x, double y, double z);
}