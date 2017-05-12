package com.kylecorry.imageEnhancement.imageProcessing;

import com.kylecorry.imageEnhancement.Main;
import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

class HDR {

    private FileManager fileManager;

    HDR(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    IntegerProperty imageNumber = new SimpleIntegerProperty(1, "imageNumber");

    Mat reduceNoise(List<String> imageFiles) {
        imageNumber.set(1);
        Mat current = fileManager.openImage(imageFiles.get(0));
        Mat average = Mat.zeros(current.size(), CvType.CV_32FC(3));
        Imgproc.accumulate(current, average);
        for (int i = 1; i < imageFiles.size(); i++) {
            imageNumber.set(i + 1);
            current = fileManager.openImage(imageFiles.get(i));
            Imgproc.accumulate(current, average);
            current.release();
        }
        current.release();
        Core.divide(average, Scalar.all(imageFiles.size()), average);
        return average;
    }

    BufferedImage averageImages(List<String> imageFiles) {
        imageNumber.set(1);
        BufferedImage current;
        BufferedImage average = fileManager.loadImage(imageFiles.get(0));
        System.out.println("Processing image 1 of " + imageFiles.size());

        Graphics2D graphics2D = average.createGraphics();

        for (int i = 1; i < imageFiles.size(); i++) {
            imageNumber.set(i + 1);
            System.out.println("Processing image " + (i + 1) + " of " + imageFiles.size());
            current = ImageUtils.copyImage(fileManager.loadImage(imageFiles.get(i)), BufferedImage.TYPE_INT_ARGB);
            float[] scales = {1f, 1f, 1f, (float) (1.0 / (i + 1))};
            float[] offsets = new float[4];
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            graphics2D.drawImage(current, rop, 0, 0);
        }
        graphics2D.dispose();

        return average;
    }
}
