package com.kylecorry.imageEnhancement.imageProcessing;

import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Kylec on 5/11/2017.
 */
public class ImageProcessor {

    private FileManager fileManager;
    public IntegerProperty imageNumber = new SimpleIntegerProperty(1, "imageNumber");

    public ImageProcessor(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Mat reduceNoise(List<String> frameFileNames) {
        HDR hdr = new HDR(fileManager);
        imageNumber.bind(hdr.imageNumber);
        Mat image = hdr.reduceNoise(frameFileNames);
        imageNumber.unbind();
        return image;
    }

    public Mat subtractImages(Mat first, Mat second) {
        ImageSubtractor subtractor = new ImageSubtractor();
        return subtractor.subtract(first, second);
    }

    public Mat reduceNoise(List<String> frameFileNames, List<String> blackFrameFileNames) {
        Mat black = reduceNoise(blackFrameFileNames);
        Mat normal = reduceNoise(frameFileNames);
        return subtractImages(normal, black);
    }

    public Mat alignStars(List<String> frameFileNames, StarStreak star1, StarStreak star2) {
        StarAligner starAligner = new StarAligner(fileManager);
        imageNumber.bind(starAligner.imageNumber);
        Mat image = starAligner.alignStars(frameFileNames, star1.getStart(), star1.getEnd(), star2.getStart(), star2.getEnd());
        imageNumber.unbind();
        return image;
    }

    public BufferedImage alignStarsAndForeground(BufferedImage alignedStarsImage, BufferedImage reducedNoiseImage) {
        StarMerge starMerge = new StarMerge();
        return starMerge.mergeStars(alignedStarsImage, reducedNoiseImage);
    }

    private Color getAverage(Color color) {
        int total = color.getRed() + color.getBlue() + color.getGreen();
        int average = total / 3;
        return new Color(average, average, average);
    }

    private BufferedImage grayscale(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                Color average = getAverage(color);
                gray.setRGB(x, y, average.getRGB());
            }
        }
        return gray;
    }


}
