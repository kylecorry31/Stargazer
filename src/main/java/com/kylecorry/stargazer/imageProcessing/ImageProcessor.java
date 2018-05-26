package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.imageProcessing.stars.*;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.AutoAlign;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.ManualAlign;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.ProgressTrackableAligner;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.StarAligner;
import com.kylecorry.stargazer.storage.FileManager;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kylec on 5/11/2017.
 */
public class ImageProcessor extends ProgressTrackable {

    private final FileManager fileManager;

    public ImageProcessor(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Mat reduceNoise(List<String> frameFileNames) {
        ImageBlender imageBlender = new ImageBlender(fileManager);
        progressProperty().bind(imageBlender.progressProperty());
        Mat image = imageBlender.reduceNoise(frameFileNames);
        progressProperty().unbind();
        return image;
    }

    public Mat streakLights(List<String> frameFileNames) {
        ImageBlender imageBlender = new ImageBlender(fileManager);
        progressProperty().bind(imageBlender.progressProperty());
        Mat image = imageBlender.lighten(frameFileNames);
        progressProperty().unbind();
        return image;
    }

    public Mat subtractImages(Mat first, Mat second) {
        ImageSubtractor subtractor = new ImageSubtractor();
        return subtractor.subtract(first, second);
    }

    public Mat alignStars(List<String> frameFileNames, StarStreak star1, StarStreak star2) {
        return alignStars(new ManualAlign(fileManager, frameFileNames, star1, star2));
    }

    public Mat alignStars(List<String> lightFiles, Mat blackFile) {
        return alignStars(new AutoAlign(fileManager, lightFiles, blackFile));
    }

    public Mat alignStars(ProgressTrackableAligner alignmentTechnique) {
        StarAligner aligner = new StarAligner(alignmentTechnique);
        progressProperty().bind(aligner.progressProperty());
        Mat image = aligner.align();
        progressProperty().unbind();
        return image;
    }
}
