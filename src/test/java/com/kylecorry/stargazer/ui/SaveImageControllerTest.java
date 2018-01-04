package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.storage.MockFileManager;
import com.kylecorry.stargazer.storage.MockFileNameGenerator;
import com.kylecorry.stargazer.storage.MockFileSelector;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;

import static org.junit.Assert.*;

public class SaveImageControllerTest {

    private SaveImageController controller;

    @Before
    public void setup(){
        controller = new SaveImageController(new MockFileManager(), new MockFileSelector(), new MockFileNameGenerator());
    }

    @Test
    public void saveImage(){
        boolean saved = controller.saveImage(Mat.zeros(20, 20, CvType.CV_8UC3));
        assertTrue(saved);
        File f = new File("test.jpg");
        assertTrue(f.exists());
        f.delete();
    }

}