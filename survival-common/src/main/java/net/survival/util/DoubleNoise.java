package net.survival.util;

import static java.lang.Math.*;
import static net.survival.util.MathEx.*;

public class DoubleNoise
{
    private DoubleNoise() {}

    public static double white(double x, long seed) {
        var xi = (int) Math.floor(x);
        var intValue = IntNoise.white(xi, seed);
        return (double) intValue / Integer.MAX_VALUE;
    }

    public static double white2D(double x, double y, long seed) {
        var xi = (int) Math.floor(x);
        var yi = (int) Math.floor(y);
        var intValue = IntNoise.white2D(xi, yi, seed);
        return (double) intValue / Integer.MAX_VALUE;
    }

    public static double white3D(double x, double y, double z, long seed) {
        var xi = (int) Math.floor(x);
        var yi = (int) Math.floor(y);
        var zi = (int) Math.floor(z);
        var intValue = IntNoise.white3D(xi, yi, zi, seed);
        return (double) intValue / Integer.MAX_VALUE;
    }

    public static double linear(double x, long seed) {
        var a = white(floor(x), seed);
        var b = white(ceil(x), seed);
        var t = x - Math.floor(x);
        return lerp(a, b, t);
    }

    public static double linear2D(double x, double y, long seed) {
        var xt = x - floor(x);
        var yt = y - floor(y);

        var tl = white2D(floor(x), floor(y), seed);
        var tr = white2D(ceil(x), floor(y), seed);
        var bl = white2D(floor(x), ceil(y), seed);
        var br = white2D(ceil(x), ceil(y), seed);

        var top = lerp(tl, tr, xt);
        var bottom = lerp(bl, br, xt);

        var value = lerp(top, bottom, yt);
        return value;
    }

    public static double linear3D(double x, double y, double z, long seed) {
        // *BL: back-left (-x, -z)
        // *BR: back-right (+x, -z)
        // *FL: front-left (-x, +z)
        // *FR: front-right (+x, +z)

        var xf = floor(x);
        var yf = floor(y);
        var zf = floor(z);
        var xt = x - xf;
        var yt = y - yf;
        var zt = z - zf;

        var bottomBL = white3D(xf, yf, zf, seed);
        var bottomBR = white3D(xf + 1.0, yf, zf, seed);
        var bottomFL = white3D(xf, yf, zf + 1.0, seed);
        var bottomFR = white3D(xf + 1.0, yf, zf + 1.0, seed);

        var topBL = white3D(xf, yf + 1.0, zf, seed);
        var topBR = white3D(xf + 1.0, yf + 1.0, zf, seed);
        var topFL = white3D(xf, yf + 1.0, zf + 1.0, seed);
        var topFR = white3D(xf + 1.0, yf + 1.0, zf + 1.0, seed);

        var bottomB = lerp(bottomBL, bottomBR, xt);
        var bottomF = lerp(bottomFL, bottomFR, xt);
        var bottom = lerp(bottomB, bottomF, zt);

        var topB = lerp(topBL, topBR, xt);
        var topF = lerp(topFL, topFR, xt);
        var top = lerp(topB, topF, zt);

        var value = lerp(bottom, top, yt);
        return value;
    }

    public static double perlinOctave2D(double x, double y, long seed) {
        var xf = floor(x);
        var yf = floor(y);
        var xt = x - xf;
        var yt = y - yf;

        var tlGradX = white2D(xf, yf, seed);
        var tlGradY = white2D(xf, yf, seed + 1L);
        var trGradX = white2D(xf + 1.0, yf, seed);
        var trGradY = white2D(xf + 1.0, yf, seed + 1L);
        var blGradX = white2D(xf, yf + 1.0, seed);
        var blGradY = white2D(xf, yf + 1.0, seed + 1L);
        var brGradX = white2D(xf + 1.0, yf + 1.0, seed);
        var brGradY = white2D(xf + 1.0, yf + 1.0, seed + 1L);

        var tlDistX = xt;
        var tlDistY = yt;
        var trDistX = -(1.0 - xt);
        var trDistY = yt;
        var blDistX = xt;
        var blDistY = -(1.0 - yt);
        var brDistX = -(1.0 - xt);
        var brDistY = -(1.0 - yt);

        var tlDotProduct = (tlGradX * tlDistX) + (tlGradY * tlDistY);
        var trDotProduct = (trGradX * trDistX) + (trGradY * trDistY);
        var blDotProduct = (blGradX * blDistX) + (blGradY * blDistY);
        var brDotProduct = (brGradX * brDistX) + (brGradY * brDistY);

        var interpX = perlinFade(xt);
        var interpY = perlinFade(yt);

        var top = lerp(tlDotProduct, trDotProduct, interpX);
        var bottom = lerp(blDotProduct, brDotProduct, interpX);

        var value = lerp(top, bottom, interpY);
        return value;
    }

    public static double perlinOctave3D(double x, double y, double z, long seed) {
        var xf = floor(x);
        var yf = floor(y);
        var zf = floor(z);
        var ceilX = xf + 1.0;
        var ceilY = yf + 1.0;
        var ceilZ = zf + 1.0;
        var xt = x - xf;
        var yt = y - yf;
        var zt = z - zf;

        var seed1 = seed + 1L;
        var seed2 = seed + 2L;

        // b**: -Y, t**: +Y
        // *b*: -Z, *f*: +Z
        // **l: -X, **r: +X
        var bblGradX = white3D(xf, yf, zf, seed);
        var bblGradY = white3D(xf, yf, zf, seed1);
        var bblGradZ = white3D(xf, yf, zf, seed2);
        var bbrGradX = white3D(ceilX, yf, zf, seed);
        var bbrGradY = white3D(ceilX, yf, zf, seed1);
        var bbrGradZ = white3D(ceilX, yf, zf, seed2);
        var bflGradX = white3D(xf, yf, ceilZ, seed);
        var bflGradY = white3D(xf, yf, ceilZ, seed1);
        var bflGradZ = white3D(xf, yf, ceilZ, seed2);
        var bfrGradX = white3D(ceilX, yf, ceilZ, seed);
        var bfrGradY = white3D(ceilX, yf, ceilZ, seed1);
        var bfrGradZ = white3D(ceilX, yf, ceilZ, seed2);
        var tblGradX = white3D(xf, ceilY, zf, seed);
        var tblGradY = white3D(xf, ceilY, zf, seed1);
        var tblGradZ = white3D(xf, ceilY, zf, seed2);
        var tbrGradX = white3D(ceilX, ceilY, zf, seed);
        var tbrGradY = white3D(ceilX, ceilY, zf, seed1);
        var tbrGradZ = white3D(ceilX, ceilY, zf, seed2);
        var tflGradX = white3D(xf, ceilY, ceilZ, seed);
        var tflGradY = white3D(xf, ceilY, ceilZ, seed1);
        var tflGradZ = white3D(xf, ceilY, ceilZ, seed2);
        var tfrGradX = white3D(ceilX, ceilY, ceilZ, seed);
        var tfrGradY = white3D(ceilX, ceilY, ceilZ, seed1);
        var tfrGradZ = white3D(ceilX, ceilY, ceilZ, seed2);

        var topDistY = yt - 1.0;
        var bottomDistY = yt;
        var leftDistX = xt;
        var rightDistX = xt - 1.0;
        var backDistZ = zt;
        var frontDistZ = zt - 1.0;

        var bblDotProduct = (bblGradX * leftDistX) + (bblGradY * bottomDistY)
                + (bblGradZ * backDistZ);
        var bbrDotProduct = (bbrGradX * rightDistX) + (bbrGradY * bottomDistY)
                + (bbrGradZ * backDistZ);
        var bflDotProduct = (bflGradX * leftDistX) + (bflGradY * bottomDistY)
                + (bflGradZ * frontDistZ);
        var bfrDotProduct = (bfrGradX * rightDistX) + (bfrGradY * bottomDistY)
                + (bfrGradZ * frontDistZ);
        var tblDotProduct = (tblGradX * leftDistX) + (tblGradY * topDistY)
                + (tblGradZ * backDistZ);
        var tbrDotProduct = (tbrGradX * rightDistX) + (tbrGradY * topDistY)
                + (tbrGradZ * backDistZ);
        var tflDotProduct = (tflGradX * leftDistX) + (tflGradY * topDistY)
                + (tflGradZ * frontDistZ);
        var tfrDotProduct = (tfrGradX * rightDistX) + (tfrGradY * topDistY)
                + (tfrGradZ * frontDistZ);

        var interpX = perlinFade(xt);
        var interpY = perlinFade(yt);
        var interpZ = perlinFade(zt);

        var bottomBack = lerp(bblDotProduct, bbrDotProduct, interpX);
        var bottomFront = lerp(bflDotProduct, bfrDotProduct, interpX);
        var bottom = lerp(bottomBack, bottomFront, interpZ);
        var topBack = lerp(tblDotProduct, tbrDotProduct, interpX);
        var topFront = lerp(tflDotProduct, tfrDotProduct, interpX);
        var top = lerp(topBack, topFront, interpZ);

        var value = lerp(bottom, top, interpY);
        return value;
    }

    public static double perlin2D(
            double x,
            double y,
            int octaveCount,
            double persistence,
            long seed)
    {
        var value = 0.0;
        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var i = 0; i < octaveCount; ++i) {
            var octave = perlinOctave2D(
                    x * frequency,
                    y * frequency,
                    seed);
            value += octave * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2.0;
        }

        value /= maxValue;
        return value;
    }

    public static double perlin3D(
            double x,
            double y,
            double z,
            int octaveCount,
            double persistence,
            long seed)
    {
        var value = 0.0;
        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var i = 0; i < octaveCount; ++i) {
            var octave = perlinOctave3D(
                    x * frequency,
                    y * frequency,
                    z * frequency,
                    seed);
            value += octave * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2.0;
        }

        value /= maxValue;
        return value;
    }

    public static double improvedOctave2D(double x, double y, long seed) {
        var xf = floor(x);
        var yf = floor(y);
        var xfi = (int) xf;
        var yfi = (int) yf;
        var xt = x - xf;
        var yt = y - yf;

        var tlHash = IntNoise.white2D(xfi,     yfi,     seed);
        var trHash = IntNoise.white2D(xfi + 1, yfi,     seed);
        var blHash = IntNoise.white2D(xfi,     yfi + 1, seed);
        var brHash = IntNoise.white2D(xfi + 1, yfi + 1, seed);

        var tlDotProduct = gradDotDist2D(tlHash, xt,       yt);
        var trDotProduct = gradDotDist2D(trHash, xt - 1.0, yt);
        var blDotProduct = gradDotDist2D(blHash, xt,       yt - 1.0);
        var brDotProduct = gradDotDist2D(brHash, xt - 1.0, yt - 1.0);

        var interpX = perlinFade(xt);
        var interpY = perlinFade(yt);

        var top = lerp(tlDotProduct, trDotProduct, interpX);
        var bottom = lerp(blDotProduct, brDotProduct, interpX);

        var value = lerp(top, bottom, interpY);
        return value;
    }

    public static double improvedOctave3D(double x, double y, double z, long seed) {
        var xf = floor(x);
        var yf = floor(y);
        var zf = floor(z);
        var xfi = (int) xf;
        var yfi = (int) yf;
        var zfi = (int) zf;
        var xt = x - xf;
        var yt = y - yf;
        var zt = z - zf;

        var bblHash = IntNoise.white3D(xfi,     yfi,     zfi,     seed);
        var bbrHash = IntNoise.white3D(xfi + 1, yfi,     zfi,     seed + 1L);
        var bflHash = IntNoise.white3D(xfi,     yfi,     zfi + 1, seed + 2L);
        var bfrHash = IntNoise.white3D(xfi + 1, yfi,     zfi + 1, seed + 3L);
        var tblHash = IntNoise.white3D(xfi,     yfi + 1, zfi,     seed + 4L);
        var tbrHash = IntNoise.white3D(xfi + 1, yfi + 1, zfi,     seed + 5L);
        var tflHash = IntNoise.white3D(xfi,     yfi + 1, zfi + 1, seed + 6L);
        var tfrHash = IntNoise.white3D(xfi + 1, yfi + 1, zfi + 1, seed + 7L);

        var bblDotProduct = gradDotDist3D(bblHash, xt,       yt,       zt);
        var bbrDotProduct = gradDotDist3D(bbrHash, xt - 1.0, yt,       zt);
        var bflDotProduct = gradDotDist3D(bflHash, xt,       yt,       zt - 1.0);
        var bfrDotProduct = gradDotDist3D(bfrHash, xt - 1.0, yt,       zt - 1.0);
        var tblDotProduct = gradDotDist3D(tblHash, xt,       yt - 1.0, zt);
        var tbrDotProduct = gradDotDist3D(tbrHash, xt - 1.0, yt - 1.0, zt);
        var tflDotProduct = gradDotDist3D(tflHash, xt,       yt - 1.0, zt - 1.0);
        var tfrDotProduct = gradDotDist3D(tfrHash, xt - 1.0, yt - 1.0, zt - 1.0);

        var interpX = perlinFade(xt);
        var interpY = perlinFade(yt);
        var interpZ = perlinFade(zt);

        var bottomBack = lerp(bblDotProduct, bbrDotProduct, interpX);
        var bottomFront = lerp(bflDotProduct, bfrDotProduct, interpX);
        var bottom = lerp(bottomBack, bottomFront, interpZ);
        var topBack = lerp(tblDotProduct, tbrDotProduct, interpX);
        var topFront = lerp(tflDotProduct, tfrDotProduct, interpX);
        var top = lerp(topBack, topFront, interpZ);

        var value = lerp(bottom, top, interpY);
        return value;
    }

    public static double improved2D(
            double x,
            double y,
            int octaveCount,
            double persistence,
            long seed)
    {
        var value = 0.0;
        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var i = 0; i < octaveCount; ++i) {
            var octave = improvedOctave2D(x * frequency, y * frequency, seed);
            value += octave * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2.0;
        }

        value /= maxValue;
        return value;
    }

    public static double improved3D(
            double x,
            double y,
            double z,
            int octaveCount,
            double persistence,
            long seed)
    {
        var value = 0.0;
        var frequency = 1.0;
        var amplitude = 1.0;
        var maxValue = 0.0;

        for (var i = 0; i < octaveCount; ++i) {
            var octave = improvedOctave3D(
                    x * frequency,
                    y * frequency,
                    z * frequency,
                    seed);
            value += octave * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2.0;
        }

        value /= maxValue;
        return value;
    }

    private static double gradDotDist2D(int hash, double x, double y) {
        switch (hash & 7) {
        case 0:  return +x    ;
        case 1:  return -x    ;
        case 2:  return    + y;
        case 3:  return    - y;
        case 4:  return +x + y;
        case 5:  return -x + y;
        case 6:  return +x - y;
        case 7:  return -x - y;
        default: return 0.0;
        }
    }

    private static double gradDotDist3D(int hash, double x, double y, double z) {
        switch (hash & 15) {
        case 0:  return +x +y   ;
        case 1:  return -x +y   ;
        case 2:  return +x -y   ;
        case 3:  return -x -y   ;
        case 4:  return +x    +z;
        case 5:  return -x    +z;
        case 6:  return +x    -z;
        case 7:  return -x    -z;
        case 8:  return    +y +z;
        case 9:  return    -y +z;
        case 10: return    +y -z;
        case 11: return    -y -z;
        case 12: return +x +y   ;
        case 13: return    -y +z;
        case 14: return -x +y   ;
        case 15: return    -y -z;
        default: return 0.0;
        }
    }

    private static double perlinFade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
}