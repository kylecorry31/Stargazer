package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.imageProcessing.stars.*;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.AutoAlign;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.ManualAlign;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.ProgressTrackableAligner;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.StarAligner;
import com.kylecorry.stargazer.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;
import java.util.PriorityQueue;

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
        return alignStars(new ManualAlign(fileManager, frameFileNames, star1, star2));
    }

    public PriorityQueue<StarFinder.StarPair> matchStars(List<Point> first, List<Point> second) {
        StarFinder finder = new StarFinder();
        return finder.matchStars(first, second);
    }

    public Mat alignStars(List<String> lightFiles, Mat blackFile) {
        return alignStars(new AutoAlign(fileManager, lightFiles, blackFile));
    }

    public Mat alignStars(ProgressTrackableAligner alignmentTechnique) {
        StarAligner aligner = new StarAligner(alignmentTechnique);
        imageNumber.bind(aligner.progressProperty());
        Mat image = aligner.align();
        imageNumber.unbind();
        return image;
    }
}
