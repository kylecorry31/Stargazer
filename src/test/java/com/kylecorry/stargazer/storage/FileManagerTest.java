package com.kylecorry.stargazer.storage;

import com.kylecorry.stargazer.OpenCVManager;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Kylec on 5/14/2017.
 */
public class FileManagerTest {

    private FileManager fileManager;

    @Before
    public void setup() {
        OpenCVManager.load();
        fileManager = new FileManager();
    }

    @Test
    public void getAllFileNamesInDirectory() throws Exception {
        File directory = new File("testingDir");
        directory.mkdir();
        File f1 = new File("testingDir/test1.txt");
        File f2 = new File("testingDir/test2.txt");
        f1.createNewFile();
        f2.createNewFile();
        List<String> files = fileManager.getAllFileNamesInDirectory("testingDir");
        List<String> files2 = fileManager.getAllFileNamesInDirectory(directory);
        assertEquals(files, files2);
        List<String> myFiles = Arrays.asList(f1.getAbsolutePath(), f2.getAbsolutePath());
        assertEquals(files, myFiles);
        f1.delete();
        f2.delete();
        directory.delete();
    }

    @Test
    public void testDirectoryNotExists() throws Exception {
        List<String> shouldBeEmpty = fileManager.getAllFileNamesInDirectory("testingDir");
        assertTrue(shouldBeEmpty.isEmpty());
        File f1 = new File("test1.txt");
        f1.createNewFile();
        List<String> alsoShouldBeEmpty = fileManager.getAllFileNamesInDirectory("test1.txt");
        assertTrue(alsoShouldBeEmpty.isEmpty());
        f1.delete();
    }

    @Test
    public void saveAndLoadImage() {
        Mat image = Mat.zeros(10, 10, CvType.CV_8UC3);
        fileManager.saveImage(image, "test.jpg");
        File file = new File("test.jpg");
        assertTrue(file.exists());
        Mat retrieved = fileManager.loadImage("test.jpg");
        Core.subtract(image, retrieved, image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        assertEquals(0, Core.countNonZero(image));
        image.release();
        retrieved.release();
        fileManager.deleteFile("test.jpg");
    }

    @Test
    public void deleteFile() throws Exception {
        File file = new File("test.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        assertTrue(fileManager.deleteFile("test.txt"));
        assertFalse(file.exists());
    }

}