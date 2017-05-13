package com.kylecorry.stargazer.storage;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kylec on 5/11/2017.
 */
public class FileManager {

    public List<String> getAllFileNamesInDirectory(File folder) {
        List<String> files = new LinkedList<>();

        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < (listOfFiles != null ? listOfFiles.length : 0); i++) {
            if (listOfFiles[i].isFile()) {
                files.add(folder.getAbsolutePath() + "/" + listOfFiles[i].getName());
            }
        }
        return files;
    }

    public List<String> getAllFileNamesInDirectory(String directory) {
        return getAllFileNamesInDirectory(new File(directory));
    }

    public Mat loadImage(String filename) {
        return Imgcodecs.imread(filename);
    }

    public void saveImage(Mat image, String filename) {
        Imgcodecs.imwrite(filename, image);
    }

}
