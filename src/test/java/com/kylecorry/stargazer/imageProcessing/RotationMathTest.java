package com.kylecorry.stargazer.imageProcessing;

import org.junit.Test;
import org.opencv.core.Point;

import static org.junit.Assert.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class RotationMathTest {

    @Test
    public void centerOfRotation() {

        Point a = new Point(10, -33);
        Point b = new Point(10, -18);
        Point c = new Point(30, -18);
        Point aPrime = new Point(-6, 39);
        Point bPrime = new Point(-15, 27);
        Point cPrime = new Point(-31, 39);

        assertEquals(new RotationMath.Pair(2, 3), RotationMath.midPoint(a, aPrime));
        assertEquals(-9 / 2.0, RotationMath.slope(a, aPrime), 0.001);
        assertTrue(pointsEqual(new Point(-10, 1 / 3.0), RotationMath.centerOfRotation(a, aPrime, b, bPrime), 0.0001));
        assertTrue(pointsEqual(new Point(-10, 1 / 3.0), RotationMath.centerOfRotation(c, cPrime, a, aPrime), 0.0001));
    }

    @Test
    public void angleBetween() {
        Point a = new Point(10, -33);
        Point b = new Point(10, -18);
        Point c = new Point(30, -18);
        Point aPrime = new Point(-6, 39);
        Point bPrime = new Point(-15, 27);
        Point cPrime = new Point(-31, 39);

        assertEquals(Math.toRadians(143), RotationMath.angleBetween(new Point(-10, 1 / 3.0), a, aPrime), 0.1);
        assertEquals(Math.toRadians(143), RotationMath.angleBetween(new Point(-10, 1 / 3.0), b, bPrime), 0.1);
        assertEquals(Math.toRadians(143), RotationMath.angleBetween(new Point(-10, 1 / 3.0), c, cPrime), 0.1);
    }

    private boolean pointsEqual(Point first, Point second, double thresh) {
        return Math.abs(first.x - second.x) <= thresh && Math.abs(first.y - second.y) <= thresh;
    }

}