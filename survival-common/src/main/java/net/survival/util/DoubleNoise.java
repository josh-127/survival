package net.survival.util;

import static java.lang.Math.*;
import static net.survival.util.MathEx.*;

public class DoubleNoise
{
    private DoubleNoise() {}

    public static double white(double x, long seed) {
        int xi = (int) Math.floor(x);
        int intValue = IntNoise.white(xi, seed);
        return (double) intValue / Integer.MAX_VALUE;
    }

    public static double white2D(double x, double y, long seed) {
        int xi = (int) Math.floor(x);
        int yi = (int) Math.floor(y);
        int intValue = IntNoise.white2D(xi, yi, seed);
        return (double) intValue / Integer.MAX_VALUE;
    }

    public static double white3D(double x, double y, double z, long seed) {
        int xi = (int) Math.floor(x);
        int yi = (int) Math.floor(y);
        int zi = (int) Math.floor(z);
        int intValue = IntNoise.white3D(xi, yi, zi, seed);
        return (double) intValue / Integer.MAX_VALUE;
    }

    public static double linear(double x, long seed) {
        double a = white(floor(x), seed);
        double b = white(ceil(x), seed);
        double t = x - Math.floor(x);
        return lerp(a, b, t);
    }

    public static double linear2D(double x, double y, long seed) {
        double xt = x - floor(x);
        double yt = y - floor(y);
        
        double tl = white2D(floor(x), floor(y), seed);
        double tr = white2D(ceil(x), floor(y), seed);
        double bl = white2D(floor(x), ceil(y), seed);
        double br = white2D(ceil(x), ceil(y), seed);

        double top = lerp(tl, tr, xt);
        double bottom = lerp(bl, br, xt);

        double value = lerp(top, bottom, yt);
        return value;
    }

    public static double linear3D(double x, double y, double z, long seed) {
        // *BL: back-left   (-x, -z)
        // *BR: back-right  (+x, -z)
        // *FL: front-left  (-x, +z)
        // *FR: front-right (+x, +z)
        
        double xf = floor(x);
        double yf = floor(y);
        double zf = floor(z);
        double xt = x - xf;
        double yt = y - yf;
        double zt = z - zf;
        
        double bottomBL = white3D(xf,       yf, zf,       seed);
        double bottomBR = white3D(xf + 1.0, yf, zf,       seed);
        double bottomFL = white3D(xf,       yf, zf + 1.0, seed);
        double bottomFR = white3D(xf + 1.0, yf, zf + 1.0, seed);

        double topBL = white3D(xf,       yf + 1.0, zf,       seed);
        double topBR = white3D(xf + 1.0, yf + 1.0, zf,       seed);
        double topFL = white3D(xf,       yf + 1.0, zf + 1.0, seed);
        double topFR = white3D(xf + 1.0, yf + 1.0, zf + 1.0, seed);

        double bottomB = lerp(bottomBL, bottomBR, xt);
        double bottomF = lerp(bottomFL, bottomFR, xt);
        double bottom = lerp(bottomB, bottomF, zt);

        double topB = lerp(topBL, topBR, xt);
        double topF = lerp(topFL, topFR, xt);
        double top = lerp(topB, topF, zt);

        double value = lerp(bottom, top, yt);
        return value;
    }

    public static double perlinOctave2D(double x, double y, long seed) {
        double xf = floor(x);
        double yf = floor(y);
        double xt = x - xf;
        double yt = y - yf;
        
        double tlGradX = white2D(xf,       yf,       seed     );
        double tlGradY = white2D(xf,       yf,       seed + 1L);
        double trGradX = white2D(xf + 1.0, yf,       seed     );
        double trGradY = white2D(xf + 1.0, yf,       seed + 1L);
        double blGradX = white2D(xf,       yf + 1.0, seed     );
        double blGradY = white2D(xf,       yf + 1.0, seed + 1L);
        double brGradX = white2D(xf + 1.0, yf + 1.0, seed     );
        double brGradY = white2D(xf + 1.0, yf + 1.0, seed + 1L);

        double tlDistX = xt;
        double tlDistY = yt;
        double trDistX = -(1.0 - xt);
        double trDistY = yt;
        double blDistX = xt;
        double blDistY = -(1.0 - yt);
        double brDistX = -(1.0 - xt);
        double brDistY = -(1.0 - yt);

        double tlDotProduct = (tlGradX * tlDistX) + (tlGradY * tlDistY);
        double trDotProduct = (trGradX * trDistX) + (trGradY * trDistY);
        double blDotProduct = (blGradX * blDistX) + (blGradY * blDistY);
        double brDotProduct = (brGradX * brDistX) + (brGradY * brDistY);

        double interpX = perlinFade(xt);
        double interpY = perlinFade(yt);

        double top = lerp(tlDotProduct, trDotProduct, interpX);
        double bottom = lerp(blDotProduct, brDotProduct, interpX);

        double value = lerp(top, bottom, interpY);
        return value;
    }
    
    public static double perlinOctave3D(double x, double y, double z, long seed) {
        double xf = floor(x);
        double yf = floor(y);
        double zf = floor(z);
        double ceilX = xf + 1.0;
        double ceilY = yf + 1.0;
        double ceilZ = zf + 1.0;
        double xt = x - xf;
        double yt = y - yf;
        double zt = z - zf;
        
        long seed1 = seed + 1L;
        long seed2 = seed + 2L;
        
        // b**: -Y,     t**: +Y
        // *b*: -Z,     *f*: +Z
        // **l: -X,     **r: +X
        double bblGradX = white3D(xf,    yf,    zf,    seed);
        double bblGradY = white3D(xf,    yf,    zf,    seed1);
        double bblGradZ = white3D(xf,    yf,    zf,    seed2);
        double bbrGradX = white3D(ceilX, yf,    zf,    seed);
        double bbrGradY = white3D(ceilX, yf,    zf,    seed1);
        double bbrGradZ = white3D(ceilX, yf,    zf,    seed2);
        double bflGradX = white3D(xf,    yf,    ceilZ, seed);
        double bflGradY = white3D(xf,    yf,    ceilZ, seed1);
        double bflGradZ = white3D(xf,    yf,    ceilZ, seed2);
        double bfrGradX = white3D(ceilX, yf,    ceilZ, seed);
        double bfrGradY = white3D(ceilX, yf,    ceilZ, seed1);
        double bfrGradZ = white3D(ceilX, yf,    ceilZ, seed2);
        double tblGradX = white3D(xf,    ceilY, zf,    seed);
        double tblGradY = white3D(xf,    ceilY, zf,    seed1);
        double tblGradZ = white3D(xf,    ceilY, zf,    seed2);
        double tbrGradX = white3D(ceilX, ceilY, zf,    seed);
        double tbrGradY = white3D(ceilX, ceilY, zf,    seed1);
        double tbrGradZ = white3D(ceilX, ceilY, zf,    seed2);
        double tflGradX = white3D(xf,    ceilY, ceilZ, seed);
        double tflGradY = white3D(xf,    ceilY, ceilZ, seed1);
        double tflGradZ = white3D(xf,    ceilY, ceilZ, seed2);
        double tfrGradX = white3D(ceilX, ceilY, ceilZ, seed);
        double tfrGradY = white3D(ceilX, ceilY, ceilZ, seed1);
        double tfrGradZ = white3D(ceilX, ceilY, ceilZ, seed2);
        
        double topDistY    = yt - 1.0;
        double bottomDistY = yt;
        double leftDistX   = xt;
        double rightDistX  = xt - 1.0;
        double backDistZ   = zt;
        double frontDistZ  = zt - 1.0;
        
        double bblDotProduct = (bblGradX * leftDistX)  + (bblGradY * bottomDistY) + (bblGradZ * backDistZ);
        double bbrDotProduct = (bbrGradX * rightDistX) + (bbrGradY * bottomDistY) + (bbrGradZ * backDistZ);
        double bflDotProduct = (bflGradX * leftDistX)  + (bflGradY * bottomDistY) + (bflGradZ * frontDistZ);
        double bfrDotProduct = (bfrGradX * rightDistX) + (bfrGradY * bottomDistY) + (bfrGradZ * frontDistZ);
        double tblDotProduct = (tblGradX * leftDistX)  + (tblGradY * topDistY)    + (tblGradZ * backDistZ);
        double tbrDotProduct = (tbrGradX * rightDistX) + (tbrGradY * topDistY)    + (tbrGradZ * backDistZ);
        double tflDotProduct = (tflGradX * leftDistX)  + (tflGradY * topDistY)    + (tflGradZ * frontDistZ);
        double tfrDotProduct = (tfrGradX * rightDistX) + (tfrGradY * topDistY)    + (tfrGradZ * frontDistZ);
        
        double interpX = perlinFade(xt);
        double interpY = perlinFade(yt);
        double interpZ = perlinFade(zt);
        
        double bottomBack  = lerp(bblDotProduct, bbrDotProduct, interpX);
        double bottomFront = lerp(bflDotProduct, bfrDotProduct, interpX);
        double bottom      = lerp(bottomBack, bottomFront, interpZ);
        double topBack     = lerp(tblDotProduct, tbrDotProduct, interpX);
        double topFront    = lerp(tflDotProduct, tfrDotProduct, interpX);
        double top         = lerp(topBack, topFront, interpZ);
        
        double value = lerp(bottom, top, interpY);
        return value;
    }

    public static double perlin2D(double x, double y, int octaveCount, long seed) {
        double value = 0.0;
        double scale = 1.0;

        for (int i = 0; i < octaveCount; ++i) {
            value += perlinOctave2D(x * scale, y * scale, seed) / scale;
            scale *= 2.0;
        }

        value /= (2.0 - 1.0 / scale);
        return value;
    }
    
    public static double perlin3D(double x, double y, double z, int octaveCount, long seed) {
        double value = 0.0;
        double scale = 1.0;

        for (int i = 0; i < octaveCount; ++i) {
            value += perlinOctave3D(x * scale, y * scale, z * scale, seed) / scale;
            scale *= 2.0;
        }

        value /= (2.0 - 1.0 / scale);
        return value;
    }

    private static double perlinFade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
}