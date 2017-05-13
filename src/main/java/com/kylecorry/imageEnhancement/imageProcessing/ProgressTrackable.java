package com.kylecorry.imageEnhancement.imageProcessing;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Kylec on 5/13/2017.
 */
public abstract class ProgressTrackable {

    private IntegerProperty progress = new SimpleIntegerProperty(0, "progress");


    public IntegerProperty progressProperty() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress.set(progress);
    }

    public int getProgress() {
        return progress.get();
    }
}
