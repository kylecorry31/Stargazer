package com.kylecorry.stargazer.storage;

import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A class to manage files.
 */
public class FileManager {

    private static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList("bmp", "dib", "jpeg", "jpg", "jpe", "jp2", "png", "webp",
                                                    "pbm", "pgm", "ppm", "sr", "ras", "tiff", "tif");

    private static final String DEFAULT_FILE_EXTENSION = "jpg";

    /**
     * Get the absolute paths to all of the files in the specified directory.
     *
     * @param directory The directory to scan.
     * @return The absolute paths of all of the files in the specified directory.
     */
    public List<String> getAllFileNamesInDirectory(File directory) {
        List<String> files = new LinkedList<>();
        if (!directory.exists() || !directory.isDirectory()) {
            return files;
        }

        File[] listOfFiles = directory.listFiles();
        for (int i = 0; i < (listOfFiles != null ? listOfFiles.length : 0); i++) {
            if (listOfFiles[i].isFile()) {
                files.add(listOfFiles[i].getAbsolutePath());
            }
        }
        return files;
    }

    /**
     * Get the absolute paths to all of the files in the specified directory.
     *
     * @param directory The name of the directory to scan.
     * @return The absolute paths of all of the files in the specified directory.
     */
    public List<String> getAllFileNamesInDirectory(String directory) {
        return getAllFileNamesInDirectory(new File(directory));
    }

    /**
     * Load an image from a file.
     *
     * @param filename The filename where the image is located at.
     * @return The image as a {@link Mat}.
     */
    public Mat loadImage(String filename) {
        return Imgcodecs.imread(filename);
    }

    /**
     * Save an image to a file.
     *
     * @param image    The image as a {@link Mat}.
     * @param filename The filename where the image should be saved.
     */
    public void saveImage(Mat image, String filename) {
        String extension = FilenameUtils.getExtension(filename);
        if(!SUPPORTED_FILE_TYPES.contains(extension.toLowerCase())){
            saveImage(image, filename + "." + DEFAULT_FILE_EXTENSION);
            File savedImage = new File(filename + "." + DEFAULT_FILE_EXTENSION);
            savedImage.renameTo(new File(filename));
        } else {
            try {
                Imgcodecs.imwrite(filename, image);
            } catch (RuntimeException e) {
                System.err.println("Could not save image to " + filename);
            }
        }
    }

    /**
     * Delete a file.
     *
     * @param filename The filename of the file to delete.
     * @return True if the file was successfully deleted.
     */
    public boolean deleteFile(String filename) {
        return new File(filename).delete();
    }

}
