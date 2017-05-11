package com.kylecorry.imageEnhancement.imageProcessing;

import com.kylecorry.imageEnhancement.imageProcessing.RotationMath;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class RotationMathTest {

    @Test
    public void centerOfRotation() throws Exception {

        Point a = new Point(10, -33);
        Point b = new Point(10, -18);
        Point c = new Point(30, -18);
        Point aPrime = new Point(-6, 39);
        Point bPrime = new Point(-15, 27);
        Point cPrime = new Point(-31, 39);

        assertEquals(new RotationMath.Pair(2, 3), RotationMath.midPoint(a, aPrime));
        assertEquals(-9 / 2.0, RotationMath.slope(a, aPrime), 0.001);
        assertEquals(new Point(-10, 0), RotationMath.centerOfRotation(a, aPrime, b, bPrime));
        assertEquals(new Point(-10, 0), RotationMath.centerOfRotation(c, cPrime, a, aPrime));

    }

    @Test
    public void angleBetween(){
        Point a = new Point(10, -33);
        Point b = new Point(10, -18);
        Point c = new Point(30, -18);
        Point aPrime = new Point(-6, 39);
        Point bPrime = new Point(-15, 27);
        Point cPrime = new Point(-31, 39);

        assertEquals(Math.toRadians(143), RotationMath.angleBetween(new Point(-10, 0), a, aPrime), 0.1);
        assertEquals(Math.toRadians(143), RotationMath.angleBetween(new Point(-10, 0), b, bPrime), 0.1);
        assertEquals(Math.toRadians(143), RotationMath.angleBetween(new Point(-10, 0), c, cPrime), 0.1);
    }

}