package com.kylecorry.imageEnhancement.storage;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public boolean saveImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "JPEG", file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveImage(BufferedImage image, String filename) {
        return saveImage(image, new File(filename));
    }

    public BufferedImage loadImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage loadImage(String filename) {
        return loadImage(new File(filename));
    }

    public Mat openImage(String filename) {
        return Imgcodecs.imread(filename);
    }

    public void saveImage(Mat image, String filename) {
        Imgcodecs.imwrite(filename, image);
    }

}
