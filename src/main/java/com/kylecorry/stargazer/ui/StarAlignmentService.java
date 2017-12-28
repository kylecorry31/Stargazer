package com.kylecorry.stargazer.ui;

import com.kylecorry.stargazer.imageProcessing.ImageProcessor;
import com.kylecorry.stargazer.imageProcessing.stars.alignment.ProgressTrackableAligner;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.opencv.core.Mat;

/**
 * Created by Kylec on 5/12/2017.
 */
class StarAlignmentService extends Service<Mat> {

    private final ImageProcessor imageProcessor;

    private final int numFiles;


    private final ProgressTrackableAligner alignmentTechnique;

    public StarAlignmentService(ImageProcessor imageProcessor, ProgressTrackableAligner alignmentTechnique, int numFiles) {
        this.imageProcessor = imageProcessor;
        this.alignmentTechnique = alignmentTechnique;
        this.numFiles = numFiles;
    }

    protected Task<Mat> createTask() {
        return new Task<Mat>() {
            @Override
            protected Mat call() {
                updateProgress(0, 1.0);

                imageProcessor.progressProperty().addListener((observable, oldValue, newValue) -> {
                    updateMessage("Aligning stars in frame " + newValue.intValue() + " of " + numFiles);
                    updateProgress(newValue.intValue() / (double) numFiles, 1.0);
                });

                return imageProcessor.alignStars(alignmentTechnique);
            }
        };
    }
}
