package com.kylecorry.imageEnhancement.ui;

import com.kylecorry.imageEnhancement.imageProcessing.ImageProcessor;
import com.kylecorry.imageEnhancement.imageProcessing.stars.IAlign;
import com.kylecorry.imageEnhancement.imageProcessing.stars.ProgressTrackableAligner;
import com.kylecorry.imageEnhancement.imageProcessing.stars.StarAligner;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Kylec on 5/12/2017.
 */
public class StarAlignmentService extends Service<Mat> {

    private ImageProcessor imageProcessor;

    private int numFiles;


    private ProgressTrackableAligner alignmentTechnique;

    public StarAlignmentService(ImageProcessor imageProcessor, ProgressTrackableAligner alignmentTechnique, int numFiles) {
        this.imageProcessor = imageProcessor;
        this.alignmentTechnique = alignmentTechnique;
        this.numFiles = numFiles;
    }

    protected Task<Mat> createTask() {
        return new Task<Mat>() {
            @Override
            protected Mat call() throws Exception {
                updateProgress(0, 1.0);

                imageProcessor.imageNumber.addListener((observable, oldValue, newValue) -> {
                    updateMessage("Aligning stars in frame " + newValue.intValue() + " of " + numFiles);
                    updateProgress(newValue.intValue() / (double) numFiles, 1.0);
                });

                return imageProcessor.alignStars(alignmentTechnique);
            }
        };
    }
}
