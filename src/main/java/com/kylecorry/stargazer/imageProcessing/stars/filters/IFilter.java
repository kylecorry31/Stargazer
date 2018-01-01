package com.kylecorry.stargazer.imageProcessing.stars.filters;

import org.opencv.core.Mat;

/**
 * Created by Kylec on 5/14/2017.
 */
public interface IFilter {
    Mat filterStars(Mat lightFrame, Mat blackFrame);

    String getName();

    FilterSettings getSettings();
}
