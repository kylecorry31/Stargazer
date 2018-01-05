package com.kylecorry.stargazer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class OpenCVTest {
    private Mat image1;
    private Mat image2;

    @Before
    public void setup() {
        OpenCVManager.getInstance().load(new SystemProperties());
        image1 = Mat.zeros(2, 2, 16);
        image2 = Mat.zeros(2, 2, 16);
        image1.setTo(Scalar.all(1.0));
        image2.setTo(Scalar.all(2.0));
    }

    @After
    public void takedown() {
        image1.release();
        image2.release();
    }

    @Test
    public void testSubtract(){
        Mat out = new Mat();
        Core.subtract(image2, image1, out);
        assertEquals(image1.size(), out.size());
        assertEquals(image1.type(), out.type());
        assertEquals(1.0, out.get(0, 0)[0], 0.001);

        Core.subtract(image1, image1, out);
        assertEquals(image1.size(), out.size());
        assertEquals(image1.type(), out.type());
        assertEquals(0.0, out.get(0, 0)[0], 0.001);

        Core.subtract(image1, image2, out);
        assertEquals(image1.size(), out.size());
        assertEquals(image1.type(), out.type());
        assertEquals(0.0, out.get(0, 0)[0], 0.001);

        out.release();
    }

    @Test
    public void testAccumulate(){
        Mat out = Mat.zeros(image1.size(), CvType.CV_32FC3);
        Imgproc.accumulate(image1, out);
        assertEquals(image1.size(), out.size());
        assertEquals(CvType.CV_32FC3, out.type());
        assertEquals(1.0, out.get(0, 0)[0], 0.001);
        Imgproc.accumulate(image2, out);
        assertEquals(image1.size(), out.size());
        assertEquals(CvType.CV_32FC3, out.type());
        assertEquals(3.0, out.get(0, 0)[0], 0.001);

        out.release();
    }

    @Test
    public void testDivide(){
        Mat out = new Mat();
        Core.divide(image2, Scalar.all(2.0), out);
        assertEquals(image1.size(), out.size());
        assertEquals(image1.type(), out.type());
        assertEquals(1.0, out.get(0, 0)[0], 0.001);

        Core.divide(image1, Scalar.all(2.0), out);
        assertEquals(image1.size(), out.size());
        assertEquals(image1.type(), out.type());
        assertEquals(0.0, out.get(0, 0)[0], 0.001);

        out.release();
    }

    @Test
    public void testConvertColor(){
        Mat out = new Mat();
        Imgproc.cvtColor(image1, out, Imgproc.COLOR_BGR2GRAY);
        assertEquals(CvType.CV_8UC1, out.type());
        assertEquals(image1.size(), out.size());

        out.release();
    }

}
