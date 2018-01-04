package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.OpenCVManager;
import javafx.scene.image.Image;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.junit.Assert.*;

public class ImageUtilsTest {

    private Mat image1;

    @Before
    public void setup() {
        OpenCVManager.load();
        image1 = Mat.zeros(2, 2, 16);
        image1.setTo(Scalar.all(2.0));
    }

    @After
    public void takedown() {
        image1.release();
    }

    @Test
    public void testToImage() {
        Image img = ImageUtils.toImage(image1);
        assertEquals(image1.width(), img.getWidth(), 0.0001);
        assertEquals(image1.height(), img.getHeight(), 0.0001);
        assertEquals(image1.get(0, 0)[0], img.getPixelReader().getColor(0, 0).getBlue() * 255, 0.0001);
    }
}