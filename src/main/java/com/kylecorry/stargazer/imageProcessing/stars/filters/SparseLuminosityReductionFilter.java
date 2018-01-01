package com.kylecorry.stargazer.imageProcessing.stars.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class SparseLuminosityReductionFilter implements IFilter {
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
        double thresh = 100;
        Mat rms = new Mat();
        Core.absdiff(img, mean, rms);
        Imgproc.threshold(img, img, thresh, 255, Imgproc.THRESH_BINARY);
        Core.bitwise_and(img, img, img, rms);
        rms.release();
        return img;
    }

    @Override
    public String getName() {
        return "Sparse Luminosity Reduction Filter";
    }
}
