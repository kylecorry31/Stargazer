package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.imageProcessing.BlendMode;
import com.kylecorry.stargazer.imageProcessing.ImageProcessor;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kylec on 5/12/2017.
 */
class HDRService extends Service<Mat> {

    private final ImageProcessor imageProcessor;
    private final List<String> lightFiles;
    private final BlendMode blendMode;

    public HDRService(ImageProcessor imageProcessor, List<String> lightFiles, BlendMode blendMode) {
        this.imageProcessor = imageProcessor;
        this.lightFiles = lightFiles;
        this.blendMode = blendMode;
    }

    public HDRService(ImageProcessor imageProcessor, List<String> lightFiles) {
        this(imageProcessor, lightFiles, BlendMode.AVERAGE);
    }

    @Override
    protected Task<Mat> createTask() {
        return new Task<Mat>() {

            @Override
            protected Mat call() {
                updateProgress(0, 1.0);

                imageProcessor.progressProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
                    updateMessage("Processing frame " + newValue.intValue() + " of " + lightFiles.size());
                    updateProgress(newValue.intValue() / (double) lightFiles.size(), 1);
                }));

                if (blendMode == BlendMode.AVERAGE) {
                    return imageProcessor.reduceNoise(lightFiles);
                } else {
                    return imageProcessor.streakLights(lightFiles);
                }
            }
        };
    }
}