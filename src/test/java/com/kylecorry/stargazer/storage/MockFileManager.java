package com.kylecorry.stargazer.storage;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MockFileManager implements IFileManager{
    @Override
    public List<String> getAllFileNamesInDirectory(File directory) {
        return Arrays.asList("test1.jpg", "test2.jpg");
    }

    @Override
    public List<String> getAllFileNamesInDirectory(String directory) {
        return Arrays.asList("test1.jpg", "test2.jpg");
    }

    @Override
    public Mat loadImage(String filename) {
        return Mat.zeros(20, 20, CvType.CV_8UC3);
    }

    @Override
    public boolean saveImage(Mat image, String filename) {
        return true;
    }

    @Override
    public boolean deleteFile(String filename) {
        return true;
    }
}
