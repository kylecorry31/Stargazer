package com.kylecorry.imageEnhancement.ui;

import com.kylecorry.imageEnhancement.imageProcessing.ImageProcessor;
import com.kylecorry.imageEnhancement.imageProcessing.StarStreak;
import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kylec on 5/12/2017.
 */
public class StarAlignmentService extends Service<Mat> {

    private ImageProcessor imageProcessor;

    private List<String> lightFiles;

    private StarStreak firstStar, secondStar;

    public StarAlignmentService(ImageProcessor imageProcessor, List<String> lightFiles, StarStreak firstStar, StarStreak secondStar) {
        this.imageProcessor = imageProcessor;
        this.lightFiles = lightFiles;
        this.firstStar = firstStar;
        this.secondStar = secondStar;
    }

    protected Task<Mat> createTask() {
        return new Task<Mat>() {
            @Override
            protected Mat call() throws Exception {
                updateProgress(0, 1.0);

                imageProcessor.imageNumber.addListener((observable, oldValue, newValue) -> {
                    updateMessage("Aligning stars in frame " + newValue.intValue() + " of " + lightFiles.size());
                    updateProgress(newValue.intValue() / (double) lightFiles.size(), 1.0);
                });

                return imageProcessor.alignStars(lightFiles, firstStar, secondStar);
            }
        };
    }
}
