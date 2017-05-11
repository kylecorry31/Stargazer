package com.kylecorry.imageEnhancement.imageProcessing;

import com.kylecorry.imageEnhancement.Main;
import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

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
