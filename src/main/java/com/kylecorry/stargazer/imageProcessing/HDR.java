package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.storage.FileManager;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.List;

class HDR extends ProgressTrackable {

    private final FileManager fileManager;

    HDR(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    Mat reduceNoise(List<String> imageFiles) {
        setProgress(1);
        Mat current = fileManager.loadImage(imageFiles.get(0));
        int type = current.type();
        Averager averager = new Averager(current.size());
        averager.accumulate(current);
        current.release();
        for (int i = 1; i < imageFiles.size(); i++) {
            setProgress(i + 1);
            current = fileManager.loadImage(imageFiles.get(i));
            averager.accumulate(current);
            current.release();
        }
        Mat average = averager.getAverage(type);
        averager.release();
        return average;
    }

}
