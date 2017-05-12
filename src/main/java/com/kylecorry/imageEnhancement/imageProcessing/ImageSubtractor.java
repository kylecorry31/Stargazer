package com.kylecorry.imageEnhancement.imageProcessing;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kylec on 5/8/2017.
 */
class ImageSubtractor {

    Mat subtract(Mat first, Mat second) {
        Mat mat = new Mat();
        Core.subtract(first, second, mat);
        return mat;
    }

    BufferedImage subtract(BufferedImage first, BufferedImage second) {
        BufferedImage difference = new BufferedImage(first.getWidth(), first.getHeight(), first.getType());
        Color pixelColors;
        for (int i = 0; i < difference.getHeight(); i++) {
            for (int j = 0; j < difference.getWidth(); j++) {
                pixelColors = subtractColors(new Color(first.getRGB(j, i)), new Color(second.getRGB(j, i)));
                int color = pixelColors.getRGB();
                difference.setRGB(j, i, color);
            }
        }
        return difference;
    }

    private Color subtractColors(Color first, Color second) {
        int red = Math.max(first.getRed() - second.getRed(), 0);
        int green = Math.max(first.getGreen() - second.getGreen(), 0);
        int blue = Math.max(first.getBlue() - second.getBlue(), 0);
        return new Color(red, green, blue);
    }

}
