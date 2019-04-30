package com.kylecorry.stargazer.imageProcessing.stars.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class BinarySparseLuminosityReductionFilter implements IFilter {

    private FilterSettings settings;
    private String rmsLowerKey = "Min difference from mean";
    private double rmsLowerDefault = 70;
    private String rmsLowerDesc = "The minimum difference from the mean to be considered a star";
    private String rmsUpperKey = "Max difference from mean";
    private double rmsUpperDefault = 255;
    private String rmsUpperDesc = "The maximum difference from the mean to considered a star";



    public BinarySparseLuminosityReductionFilter() {
        settings = new FilterSettings();
        settings.put(rmsLowerKey, new FilterSetting(rmsLowerKey, rmsLowerDefault, 0, 255, rmsLowerDesc));
        settings.put(rmsUpperKey, new FilterSetting(rmsUpperKey, rmsUpperDefault, 0, 255, rmsUpperDesc));
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
        Scalar mean = Core.mean(img);
        Mat rms = new Mat();
        Core.subtract(img, mean, rms);
        Imgproc.threshold(rms, rms, settings.get(rmsLowerKey).getValue(), settings.get(rmsUpperKey).getValue(), Imgproc.THRESH_BINARY);
        return rms;
    }

    @Override
    public String getName() {
        return "Binary Sparse Luminosity Reduction Filter";
    }

    @Override
    public FilterSettings getSettings() {
        return settings;
    }
}
