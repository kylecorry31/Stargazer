package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.OpenCVManager;
import com.kylecorry.stargazer.SystemProperties;
import com.kylecorry.stargazer.storage.FileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ImageBlenderTest {

    private Mat image1;
    private Mat image2;
    private ImageBlender imageBlender;
    private FileManager fileManager;
    private List<String> fileNames;

    @Before
    public void setup() {
        OpenCVManager.getInstance().load(new SystemProperties());
        image1 = Mat.zeros(2, 2, 16);
        image2 = Mat.zeros(2, 2, 16);
        image2.setTo(Scalar.all(2.0));
        fileManager = new FileManager();
        imageBlender = new ImageBlender(fileManager);
        fileNames = saveImages();
    }

    @After
    public void takedown(){
        image1.release();
        image2.release();
        fileNames.stream().forEach((s)-> new File(s).delete());
    }

    private List<String> saveImages(){
        String fileName1 = System.currentTimeMillis() + ".jpg";
        String fileName2 = (System.currentTimeMillis() + 1) + ".jpg";
        fileManager.saveImage(image1, fileName1);
        fileManager.saveImage(image2, fileName2);
        return Arrays.asList(fileName1, fileName2);
    }

    @Test
    public void testHDRImage() throws Exception {
        Mat out = imageBlender.reduceNoise(fileNames);
        assertEquals(CvType.CV_8UC3, out.type());
        assertEquals(1.0, out.get(0, 0)[0], 0.0001);
        assertEquals(image1.size(), out.size());
    }

    @Test
    public void testProgress() throws Exception {
        assertEquals(0, imageBlender.getProgress());
        final int[] progress = {1};
        imageBlender.progressProperty().addListener((observable, oldValue, newValue) -> {
                assertEquals(progress[0], newValue.intValue());
                progress[0]++;
                progress[0] %= 3;
        });
        Mat out = imageBlender.reduceNoise(fileNames);
        out.release();
        assertEquals(0, imageBlender.getProgress());
    }


}
