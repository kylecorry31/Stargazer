package com.kylecorry.imageEnhancement.imageProcessing;

import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * Created by Kylec on 5/8/2017.
 */
class ImageSubtractor {

    Mat subtract(Mat first, Mat second) {
        Mat mat = new Mat();
        Core.subtract(first, second, mat);
        return mat;
    }

}
