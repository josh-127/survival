package net.survival.util;

public interface DoubleSampler2D
{
    double sampleNearest(int x, int z);

    double sampleLinear(double x, double z);
}