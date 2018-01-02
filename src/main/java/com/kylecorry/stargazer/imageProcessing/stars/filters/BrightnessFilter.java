package com.kylecorry.stargazer.imageProcessing.stars.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class BrightnessFilter implements IFilter {

    private FilterSettings settings;
    private String brightnessLowerKey = "Min brightness";
    private double brightnessLowerDefault = 127;
    private String brightnessLowerDesc = "The minimum brightness to be considered a star";
    private String brightnessUpperKey = "Max brightness";
    private double brightnessUpperDefault = 255;
    private String brightnessUpperDesc = "The maximum brightness to considered a star";


    public BrightnessFilter() {
        settings = new FilterSettings();
        settings.put(brightnessLowerKey, new FilterSetting(brightnessLowerKey, brightnessLowerDefault, 0, 255, brightnessLowerDesc));
        settings.put(brightnessUpperKey, new FilterSetting(brightnessUpperKey, brightnessUpperDefault, 0, 255, brightnessUpperDesc));
    }

    @Override
    public Mat filterStars(Mat lightFrame, Mat blackFrame) {
        Mat img = new Mat();
        Imgproc.cvtColor(lightFrame, img, Imgproc.COLOR_BGR2GRAY);
        if (blackFrame.channels() == 3 || blackFrame.channels() == 4) {
            Mat dark = new Mat();
            Imgproc.cvtColor(blackFrame, dark, Imgproc.COLOR_BGR2GRAY);
            Core.subtract(img, dark, img);
        }
        Imgproc.threshold(img, img, settings.get(brightnessLowerKey).getValue(), settings.get(brightnessUpperKey).getValue(), Imgproc.THRESH_BINARY);
        return img;
    }

    @Override
    public String getName() {
        return "Brightness Filter";
    }

    @Override
    public FilterSettings getSettings() {
        return settings;
    }
}
