package com.kylecorry.stargazer.stars;

import com.kylecorry.stargazer.stars.starcombinestrategies.StarCombineStrategy;

import java.util.List;
import java.util.Objects;

/**
 * A star combiner
 */
public class StarCombiner {

    private StarCombineStrategy starCombineStrategy;

    /**
     * Default constructor
     * @param starCombineStrategy the star combiner strategy
     */
    public StarCombiner(StarCombineStrategy starCombineStrategy) {
        this.starCombineStrategy = Objects.requireNonNull(starCombineStrategy);
    }

    /**
     * Combine star images into a single image
     * @param images the images
     * @param progressTrackerStrategy the progress tracker
     * @return the combined image
     */
    public Image combine(List<Image> images, ProgressTrackerStrategy progressTrackerStrategy){
        return starCombineStrategy.combine(images, progressTrackerStrategy);
    }
}
