package com.kylecorry.stargazer.stars;

import javafx.scene.control.ProgressBar;

import java.util.Objects;

public class ProgressBarTrackerStrategy implements ProgressTrackerStrategy {

    private ProgressBar progressBar;

    public ProgressBarTrackerStrategy(ProgressBar progressBar) {
        this.progressBar = Objects.requireNonNull(progressBar);
    }

    @Override
    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}
