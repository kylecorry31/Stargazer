package com.kylecorry.stargazer.imageProcessing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

class Averager {

    private Mat accumulator;
    private int count;


    Averager(Size size, int type) {
        accumulator = Mat.zeros(size, type);
        count = 0;
    }

    Averager(Size size) {
        this(size, CvType.CV_32FC3);
    }

    void accumulate(Mat image) {
        Imgproc.accumulate(image, accumulator);
        count++;
    }

    int getCount(){
        return count;
    }

    Mat getAccumulator(){
        return accumulator;
    }

    Mat getAverage() {
        Mat average = new Mat();
        Core.divide(accumulator, Scalar.all(count), average);
        return average;
    }

    void reset() {
        accumulator.setTo(Scalar.all(0.0));
        count = 0;
    }

    public void release() {
        accumulator.release();
    }
}
