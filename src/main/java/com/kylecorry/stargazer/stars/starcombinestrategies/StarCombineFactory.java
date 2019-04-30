package com.kylecorry.stargazer.stars.starcombinestrategies;

import com.kylecorry.stargazer.imageProcessing.blendModes.Darken;
import com.kylecorry.stargazer.imageProcessing.blendModes.Lighten;
import com.kylecorry.stargazer.imageProcessing.stars.filters.SparseLuminosityReductionFilter;

/**
 * A factory for creating star combine strategies
 */
public class StarCombineFactory {

    private StarCombineFactory(){}


    /**
     * @return a star combine strategy which causes star streaks to form
     */
    public static StarCombineStrategy streakStars(){
        return new BlendedStarCombineStrategy(new Lighten());
    }

    /**
     * @return a star combine strategy which removes most stars and darkens the image
     */
    public static StarCombineStrategy removeStars() {
        return new BlendedStarCombineStrategy(new Darken());
    }

    /**
     * @return a star combine strategy which reduces noise, but allows streaks to form
     */
    public static StarCombineStrategy reduceNoise() {
        return new AverageStarCombineStrategy();
    }

    /**
     * @return a star combine strategy which aligns stars based on their pin-points
     */
    public static StarCombineStrategy alignStars() {
        return new StarAlignStarCombineStrategy(new SparseLuminosityReductionFilter());
    }

    /**
     * @return a star combine strategy which aligns stars based on their streaks
     */
    public static StarCombineStrategy alignStreaks() {
        return new StreakAlignStarCombineStrategy(new SparseLuminosityReductionFilter());
    }

}
