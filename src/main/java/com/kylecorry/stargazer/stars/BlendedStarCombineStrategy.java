package com.kylecorry.stargazer.stars;

import com.kylecorry.stargazer.imageProcessing.blendModes.Blender;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Objects;

public class BlendedStarCombineStrategy implements StarCombineStrategy {

    private Blender blender;

    /**
     * Default constructor
     * @param blender the blender to use
     */
    public BlendedStarCombineStrategy(Blender blender) {
        this.blender = Objects.requireNonNull(blender);
    }

    @Override
    public Image combine(List<Image> images, ProgressTrackerStrategy progressTrackerStrategy) {
        progressTrackerStrategy.setProgress(1.0 / images.size());
        Mat current = images.get(0).getMat();
        Mat avg = current.clone();
        current.release();
        for (int i = 1; i < images.size(); i++) {
            progressTrackerStrategy.setProgress((i + 1.0) / images.size());
            current = images.get(i).getMat();
            Mat next = blender.blend(avg, current);
            avg.release();
            avg = next;
            current.release();
        }
        return Image.fromMat(avg);
    }
}
