package net.survival.util;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class XIntegerArrayTest
{
    @Test
    public void givenRandomValues_getAndSetAreConsistent() {
        final int ARRAY_LENGTH = 4096;

        for (var bitsPerElement = 1; bitsPerElement <= 8; ++bitsPerElement) {
            var array = new XIntegerArray(ARRAY_LENGTH, bitsPerElement);
            var referenceArray = new int[ARRAY_LENGTH];
            var random = new Random(bitsPerElement * 1000L);

            for (var i = 0; i < referenceArray.length; ++i) {
                referenceArray[i] = random.nextInt(1 << bitsPerElement);
            }

            for (var i = 0; i < array.length; ++i) {
                array.set(i, referenceArray[i]);
            }

            for (var i = 0; i < array.length; ++i) {
                var expected = referenceArray[i];
                var actual = array.get(i);
                assertEquals(expected, actual);
            }
        }
    }
}