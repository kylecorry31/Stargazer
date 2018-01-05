package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.OpenCVManager;
import com.kylecorry.stargazer.SystemProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.junit.Assert.*;

public class ImageSubtractorTest {
    private Mat image1;
    private Mat image2;
    private ImageSubtractor subtractor;

    @Before
    public void setup() {
        OpenCVManager.getInstance().load(new SystemProperties());
        image1 = Mat.zeros(2, 2, 16);
        image2 = Mat.zeros(2, 2, 16);
        image1.setTo(Scalar.all(1.0));
        image2.setTo(Scalar.all(2.0));
        subtractor = new ImageSubtractor();
    }

    @After
    public void takedown() {
        image1.release();
        image2.release();
    }

    @Test
    public void subtract() {
        Mat out = subtractor.subtract(image2, image1);
        assertEquals(image1.type(), out.type());
        assertEquals(image1.size(), out.size());
        assertEquals(1.0, out.get(0, 0)[0], 0.001);
    }

    @Test
    public void subtractNegative(){
        Mat out = subtractor.subtract(image1, image2);
        assertEquals(image1.type(), out.type());
        assertEquals(image1.size(), out.size());
        assertEquals(0.0, out.get(0, 0)[0], 0.001);
    }

    @Test
    public void subtractSelf(){
        Mat out = subtractor.subtract(image1, image1);
        assertEquals(image1.type(), out.type());
        assertEquals(image1.size(), out.size());
        assertEquals(0.0, out.get(0, 0)[0], 0.001);
    }
}