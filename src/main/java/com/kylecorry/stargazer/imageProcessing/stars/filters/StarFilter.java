package com.kylecorry.stargazer.imageProcessing.stars.filters;

import org.opencv.core.Mat;

/**
 * Created by Kylec on 5/14/2017.
 */
public class StarFilter {
    private final IFilter filter;

    public StarFilter(IFilter filter) {
        this.filter = filter;
    }

    public Mat filterStars(Mat lightImage, Mat blackImage) {
        return filter.filterStars(lightImage, blackImage);
    }
}
