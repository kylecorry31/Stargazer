package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.imageProcessing.blendModes.Blender;
import com.kylecorry.stargazer.imageProcessing.blendModes.Lighten;
import com.kylecorry.stargazer.storage.FileManager;
import org.opencv.core.*;

import java.util.List;

class ImageBlender extends ProgressTrackable {

    private final FileManager fileManager;

    ImageBlender(FileManager fileManager) {
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
        setProgress(0);
        return average;
    }

    Mat lighten(List<String> imageFiles){
        Blender blender = new Lighten();
        setProgress(1);
        Mat current = fileManager.loadImage(imageFiles.get(0));
        Mat avg = current.clone();
        current.release();
        for (int i = 1; i < imageFiles.size(); i++) {
            setProgress(i + 1);
            current = fileManager.loadImage(imageFiles.get(i));
            Mat next = blender.blend(avg, current);
            avg.release();
            avg = next;
            current.release();
        }
        setProgress(0);
        return avg;
    }


    Mat darken(List<String> imageFiles){
        setProgress(1);
        Mat current = fileManager.loadImage(imageFiles.get(0));
        Mat avg = current.clone();
        current.release();
        for (int i = 1; i < imageFiles.size(); i++) {
            setProgress(i + 1);
            current = fileManager.loadImage(imageFiles.get(i));
            Core.min(avg, current, avg);
            current.release();
        }
        setProgress(0);
        return avg;
    }

}
