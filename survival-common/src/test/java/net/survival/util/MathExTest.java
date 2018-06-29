package net.survival.util;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class MathExTest
{
    private static final long SEED = 0L;
    private static final double EPSILON = 0.0009765625;
    
    private static final int LERP_MAX_ITERATIONS = 64;
    private static final double LERP_TSTEP = 0.015625;
    private static final double LERP_A_MIN = -32.0;
    private static final double LERP_A_RANGE = 64.0;
    private static final double LERP_B_RANGE = 64.0;
    
    @Test
    public void lerp_givenEndpointsAreRandomAndBIsAlwaysGreaterThanA_isInBounds() {
        Random random = new Random(SEED);
        
        for (int i = 0; i < LERP_MAX_ITERATIONS; ++i) {
            double a = random.nextDouble() * LERP_A_RANGE - LERP_A_MIN;
            double b = a + random.nextDouble() * LERP_B_RANGE;
            
            for (double t = 0.0; t <= 1.0; t += LERP_TSTEP) {
                double result = MathEx.lerp(a, b, t);
                assertTrue(result >= a && result <= b);
            }
        }
    }
    
    @Test
    public void lerp_givenEndpointsAreRandomAndBIsAlwaysGreaterThanA_isOutOfLeftBounds() {
        Random random = new Random(SEED);
        
        for (int i = 0; i < LERP_MAX_ITERATIONS; ++i) {
            double a = random.nextDouble() * LERP_A_RANGE - LERP_A_MIN;
            double b = a + random.nextDouble() * LERP_B_RANGE;
            
            if (a == b)
                continue;
            
            for (double t = -8.0; t < 0.0; t += LERP_TSTEP) {
                double result = MathEx.lerp(a, b, t);
                assertTrue(result < a);
            }
        }
    }
    
    @Test
    public void lerp_givenEndpointsAreRandomAndBIsAlwaysGreaterThanA_isOutOfRightBounds() {
        Random random = new Random(SEED);
        
        for (int i = 0; i < LERP_MAX_ITERATIONS; ++i) {
            double a = random.nextDouble() * LERP_A_RANGE - LERP_A_MIN;
            double b = a + random.nextDouble() * LERP_B_RANGE;
            
            if (a == b)
                continue;
            
            for (double t = 8.0; t > 1.0; t -= LERP_TSTEP) {
                double result = MathEx.lerp(a, b, t);
                assertTrue(result > b);
            }
        }
    }
    
    @Test
    public void lerp_givenEndpointsAreRandomAndBIsAlwaysGreaterThanA_alwaysIncreases() {
        final double TSTART = -8.0;
        final double TEND = 8.0;
        
        Random random = new Random(SEED);
        
        for (int i = 0; i < LERP_MAX_ITERATIONS; ++i) {
            double a = random.nextDouble() * LERP_A_RANGE - LERP_A_MIN;
            double b = a + random.nextDouble() * LERP_B_RANGE;
            
            if (a == b)
                continue;
            
            double prevResult = MathEx.lerp(a, b, TSTART);
            
            for (double t = TSTART + LERP_TSTEP; t <= TEND; t += LERP_TSTEP) {
                double result = MathEx.lerp(a, b, t);
                assertTrue(result > prevResult);
                prevResult = result;
            }
        }
    }
    
    @Test
    public void lerp_givenEndpointsAreRandomAndBIsAlwaysGreaterThanA_hasNoAcceleration() {
        final double TSTART = -8.0;
        final double TEND = 8.0;
        
        Random random = new Random(SEED);
        
        for (int i = 0; i < LERP_MAX_ITERATIONS; ++i) {
            double a = random.nextDouble() * LERP_A_RANGE - LERP_A_MIN;
            double b = a + random.nextDouble() * LERP_B_RANGE;
            
            if (a == b)
                continue;

            for (double t = TSTART + 2.0 * LERP_TSTEP; t <= TEND; t += LERP_TSTEP) {
                double prevPrevResult = MathEx.lerp(a, b, t - 2.0 * LERP_TSTEP);
                double prevResult = MathEx.lerp(a, b, t - LERP_TSTEP);
                double result = MathEx.lerp(a, b, t);
                assertEquals(0.0, (result - prevResult) - (prevResult - prevPrevResult), EPSILON);
            }
        }
    }
    
    @Test
    public void lerp_whenBothEndpointsAreRandomAndEqual_resultEqualsTheEndpointsRegardlessOfT() {
        final double TSTART = -8.0;
        final double TEND = 8.0;
        
        Random random = new Random(SEED);
        
        for (int i = 0; i < LERP_MAX_ITERATIONS; ++i) {
            double a = random.nextDouble() * LERP_A_RANGE - LERP_A_MIN;
            double b = a;
            
            for (double t = TSTART; t <= TEND; t += LERP_TSTEP)
                assertEquals(a, MathEx.lerp(a, b, t), EPSILON);
        }
    }
}