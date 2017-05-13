package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.List;

class HDR {

    private FileManager fileManager;

    HDR(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    IntegerProperty imageNumber = new SimpleIntegerProperty(1, "imageNumber");

    Mat reduceNoise(List<String> imageFiles) {
        imageNumber.set(1);
        Mat current = fileManager.loadImage(imageFiles.get(0));
        Mat average = Mat.zeros(current.size(), CvType.CV_32FC(3));
        Imgproc.accumulate(current, average);
        for (int i = 1; i < imageFiles.size(); i++) {
            imageNumber.set(i + 1);
            current = fileManager.loadImage(imageFiles.get(i));
            Imgproc.accumulate(current, average);
            current.release();
        }
        current.release();
        Core.divide(average, Scalar.all(imageFiles.size()), average);
        return average;
    }

}
