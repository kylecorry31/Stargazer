package com.kylecorry.imageEnhancement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class HDR {

    public IntegerProperty imageNumber = new SimpleIntegerProperty(1, "imageNumber");

    public BufferedImage averageImages(List<String> imageFiles) {
        imageNumber.set(1);
        BufferedImage current;
        BufferedImage average = Main.getImage(imageFiles.get(0));
        try {
            ImageIO.write(average, "JPEG", new File("test.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Processing image 1 of " + imageFiles.size());

        Graphics2D graphics2D = average.createGraphics();

        for (int i = 1; i < imageFiles.size(); i++) {
            imageNumber.set(i + 1);
            System.out.println("Processing image " + (i + 1) + " of " + imageFiles.size());
            current = ImageUtils.copyImage(Main.getImage(imageFiles.get(i)), BufferedImage.TYPE_INT_ARGB);
            float[] scales = {1f, 1f, 1f, (float) (1.0 / (i + 1))};
            float[] offsets = new float[4];
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            graphics2D.drawImage(current, rop, 0, 0);
        }
        graphics2D.dispose();

        return average;
    }
}
