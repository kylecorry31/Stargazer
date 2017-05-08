package com.kylecorry.imageEnhancement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
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

    public BufferedImage averageImagesLazy(List<String> imageFiles) {
        imageNumber.set(1);
        BufferedImage current = Main.getImage(imageFiles.get(0));
        BufferedImage average = new BufferedImage(current.getWidth(), current.getHeight(), current.getType());
        int[][][] pixelColors = new int[current.getHeight()][current.getWidth()][3];


        for (int i = 0; i < imageFiles.size(); i++) {
            imageNumber.set(i + 1);
            System.out.println("Processing image " + (i+1) + " of " + imageFiles.size());
            if (i != 0)
                current = Main.getImage(imageFiles.get(i));
            for (int x = 0; x < average.getWidth(); x++) {
                for (int y = 0; y < average.getHeight(); y++) {
                    Color currentColor = new Color(current.getRGB(x, y));
                    pixelColors[y][x][0] += currentColor.getRed();
                    pixelColors[y][x][1] += currentColor.getGreen();
                    pixelColors[y][x][2] += currentColor.getBlue();
                }
            }
        }

        for (int y = 0; y < pixelColors.length; y++) {
            for (int x = 0; x < pixelColors[y].length; x++) {
                int red = pixelColors[y][x][0] / imageFiles.size();
                int green = pixelColors[y][x][1] / imageFiles.size();
                int blue = pixelColors[y][x][2] / imageFiles.size();
                Color currentColor = new Color(red, green, blue);
                average.setRGB(x, y, currentColor.getRGB());
            }
        }
        return average;
    }

    public BufferedImage averageImages(List<BufferedImage> images) {
        BufferedImage average = new BufferedImage(images.get(0).getWidth(), images.get(0).getHeight(), images.get(0).getType());
        Color[] pixelColors = new Color[images.size()];
        for (int i = 0; i < average.getHeight(); i++) {
            for (int j = 0; j < average.getWidth(); j++) {
                for (int b = 0; b < images.size(); b++) {
                    pixelColors[b] = new Color(images.get(b).getRGB(j, i));
                }
                int color = getAverageColor(pixelColors).getRGB();
                average.setRGB(j, i, color);
            }
        }
        return average;
    }


//    private Color addColors(Color first, Color second) {
//        int red = Math.max(first.getRed() + second.getRed(), 0);
//        int green = Math.max(first.getGreen() + second.getGreen(), 0);
//        int blue = Math.max(first.getBlue() + second.getBlue(), 0);
//        return new Color(red, green, blue);
//    }

    private Color getAverageColor(Color[] colors) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Color color : colors) {
            red += color.getRed();
            green += color.getGreen();
            blue += color.getBlue();
        }
        red /= colors.length;
        green /= colors.length;
        blue /= colors.length;
        return new Color(red, green, blue);
    }
}
