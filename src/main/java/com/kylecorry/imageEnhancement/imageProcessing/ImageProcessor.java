package com.kylecorry.imageEnhancement.imageProcessing;

import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

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

    public BufferedImage reduceNoise(List<String> frameFileNames) {
        HDR hdr = new HDR(fileManager);
        imageNumber.bind(hdr.imageNumber);
        BufferedImage image = hdr.averageImages(frameFileNames);
        imageNumber.unbind();
        return image;
    }

    public BufferedImage subtractImages(BufferedImage first, BufferedImage second) {
        ImageSubtractor subtractor = new ImageSubtractor();
        return subtractor.subtract(first, second);
    }

    public BufferedImage reduceNoise(List<String> frameFileNames, List<String> blackFrameFileNames) {
        BufferedImage black = reduceNoise(blackFrameFileNames);
        BufferedImage normal = reduceNoise(frameFileNames);
        return subtractImages(normal, black);
    }

    public BufferedImage alignStars(List<String> frameFileNames, StarStreak star1, StarStreak star2) {
        StarAligner starAligner = new StarAligner();
        imageNumber.bind(starAligner.imageNumber);
        BufferedImage image = starAligner.alignStars(frameFileNames, star1.getStart(), star1.getEnd(), star2.getStart(), star2.getEnd());
        imageNumber.unbind();
        return image;
    }

    public BufferedImage alignStarsAndForeground(BufferedImage alignedStarsImage, BufferedImage reducedNoiseImage) {
        StarMerge starMerge = new StarMerge();
        return starMerge.mergeStars(alignedStarsImage, reducedNoiseImage);
    }


}
