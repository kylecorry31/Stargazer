package com.kylecorry.stargazer.imageProcessing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Averager {

    private Mat accumulator;
    private int count;


    public Averager(Size size, int type) {
        accumulator = Mat.zeros(size, type);
        count = 0;
    }

    public Averager(Size size) {
        this(size, CvType.CV_32FC3);
    }

    public void accumulate(Mat image) {
        Imgproc.accumulate(image, accumulator);
        count++;
    }

    public int getCount(){
        return count;
    }

    public Mat getAccumulator(){
        return accumulator;
    }

    public Mat getAverage() {
        Mat average = new Mat();
        Core.divide(accumulator, Scalar.all(count), average);
        return average;
    }

    public void reset() {
        accumulator.setTo(Scalar.all(0.0));
        count = 0;
    }

    public void release() {
        accumulator.release();
    }
}
