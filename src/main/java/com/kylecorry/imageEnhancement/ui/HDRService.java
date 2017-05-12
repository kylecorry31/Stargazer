package com.kylecorry.imageEnhancement.ui;

import com.kylecorry.imageEnhancement.imageProcessing.ImageProcessor;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kylec on 5/12/2017.
 */
class HDRService extends Service<Mat> {

    private ImageProcessor imageProcessor;
    private List<String> lightFiles;

    public HDRService(ImageProcessor imageProcessor, List<String> lightFiles) {
        this.imageProcessor = imageProcessor;
        this.lightFiles = lightFiles;
    }

    @Override
    protected Task<Mat> createTask() {
        return new Task<Mat>() {

            @Override
            protected Mat call() throws Exception {
                updateProgress(0, 1.0);

                imageProcessor.imageNumber.addListener((observable, oldValue, newValue) -> {
                    Platform.runLater(() -> {
                        updateMessage("Processing frame " + newValue.intValue() + " of " + lightFiles.size());
                        updateProgress(newValue.intValue() / (double) lightFiles.size(), 1);
                    });
                });

                return imageProcessor.reduceNoise(lightFiles);
            }
        };
    }
}