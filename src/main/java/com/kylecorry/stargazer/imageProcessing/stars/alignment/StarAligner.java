package com.kylecorry.stargazer.imageProcessing.stars.alignment;

import javafx.beans.property.IntegerProperty;
import org.opencv.core.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarAligner {

    private final ProgressTrackableAligner aligner;

    public StarAligner(ProgressTrackableAligner aligner) {
        this.aligner = aligner;
    }

    public int getProgress() {
        return aligner.getProgress();
    }

    public IntegerProperty progressProperty() {
        return aligner.progressProperty();
    }

    public Mat align() {
        return aligner.align();
    }

}
