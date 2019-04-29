package com.kylecorry.stargazer.stars;

/**
 * Tracks the progress of a process
 */
public interface ProgressTrackerStrategy {

    /**
     * @param progress the progress [0, 1]
     */
    void setProgress(double progress);

}
