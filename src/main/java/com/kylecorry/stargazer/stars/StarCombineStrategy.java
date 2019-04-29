package com.kylecorry.stargazer.stars;

import java.util.List;

/**
 * Combines images of stars together
 */
public interface StarCombineStrategy {

    /**
     * Combines star images together to form one image
     * @param images the images
     * @param progressTrackerStrategy the progress tracker
     * @return the combined image
     */
    Image combine(List<Image> images, ProgressTrackerStrategy progressTrackerStrategy);
}
