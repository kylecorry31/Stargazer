package com.kylecorry.stargazer.imageProcessing.blendModes;

import org.opencv.core.Mat;

public interface Blender {
    Mat blend(Mat a, Mat b);
}
