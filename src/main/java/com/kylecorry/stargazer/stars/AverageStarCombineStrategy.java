package com.kylecorry.stargazer.stars;

import com.kylecorry.stargazer.imageProcessing.Averager;
import org.opencv.core.Mat;

import java.util.List;

public class AverageStarCombineStrategy implements StarCombineStrategy {

    @Override
    public Image combine(List<Image> images, ProgressTrackerStrategy progressTrackerStrategy) {
        progressTrackerStrategy.setProgress(1.0 / images.size());
        Mat current = images.get(0).getMat();
        int type = current.type();
        Averager averager = new Averager(current.size());
        averager.accumulate(current);
        current.release();
        for (int i = 1; i < images.size(); i++) {
            progressTrackerStrategy.setProgress((i + 1.0) / images.size());
            current = images.get(i).getMat();
            averager.accumulate(current);
            current.release();
        }
        Mat average = averager.getAverage(type);
        averager.release();
        progressTrackerStrategy.setProgress(0);
        return Image.fromMat(average);
    }
}
