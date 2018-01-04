package com.kylecorry.stargazer.storage;

import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

public interface IFileManager {
    List<String> getAllFileNamesInDirectory(File directory);

    List<String> getAllFileNamesInDirectory(String directory);

    Mat loadImage(String filename);

    boolean saveImage(Mat image, String filename);

    boolean deleteFile(String filename);
}
