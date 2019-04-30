package com.kylecorry.stargazer.stars;

import com.kylecorry.stargazer.imageProcessing.Averager;
import com.kylecorry.stargazer.imageProcessing.stars.filters.IFilter;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.util.List;
import java.util.Objects;

public class StarAlignStarCombineStrategy implements StarCombineStrategy {

    private IFilter starFilter;

    public StarAlignStarCombineStrategy(IFilter starFilter) {
        this.starFilter = Objects.requireNonNull(starFilter);
    }

    @Override
    public Image combine(List<Image> images, ProgressTrackerStrategy progressTrackerStrategy) {
        progressTrackerStrategy.setProgress(1.0 / images.size());
        Mat current = images.get(0).getMat();
        int type = current.type();
        Mat firstStarImage = starFilter.filterStars(current, new Mat());
        Averager averager = new Averager(current.size());
        averager.accumulate(current);
        current.release();
        int warpMode = Video.MOTION_EUCLIDEAN;
        Mat warpMatrix = Mat.eye(2, 3, CvType.CV_32F);
        for (int i = 1; i < images.size(); i++) {
            progressTrackerStrategy.setProgress((i + 1.0) / images.size());
            current = images.get(i).getMat();
            Mat currentStarImage = starFilter.filterStars(current, new Mat());
            try {
                Video.findTransformECC(currentStarImage, firstStarImage, warpMatrix, warpMode);
                Imgproc.warpAffine(current, current, warpMatrix, current.size());
                averager.accumulate(current);
            } catch (Exception e) {
                System.err.println("Could not align frame " + (i + 1));
            }
            current.release();
            currentStarImage.release();
        }
        firstStarImage.release();
        warpMatrix.release();
        Mat average = averager.getAverage(type);
        averager.release();
        return Image.fromMat(average);
    }
}
