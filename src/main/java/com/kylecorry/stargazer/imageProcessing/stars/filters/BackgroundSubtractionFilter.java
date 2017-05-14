package com.kylecorry.stargazer.imageProcessing.stars.filters;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Kylec on 5/14/2017.
 */
public class BackgroundSubtractionFilter implements IFilter {


    private Mat findStars(Mat lightFrame, Mat blackFrame) {
        Mat img = new Mat();
        Imgproc.cvtColor(lightFrame, img, Imgproc.COLOR_BGR2GRAY);
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble stdev = new MatOfDouble();
        Core.meanStdDev(img, mean, stdev);
        Mat dark = new Mat();
        Imgproc.cvtColor(blackFrame, dark, Imgproc.COLOR_BGR2GRAY);
        Core.subtract(img, dark, img);
        double thresh = mean.get(0, 0)[0] + 2 * stdev.get(0, 0)[0];
        Imgproc.threshold(img, img, thresh, 255, Imgproc.THRESH_TOZERO);
        return img;
    }

    private List<Point> locateStars(Mat image, Mat blackImage) {
        Mat img = findStars(image, blackImage);
        BackgroundSubtractionGRIPFilter filter = new BackgroundSubtractionGRIPFilter();
        filter.process(img);
        List<MatOfPoint> stars = filter.filterContoursOutput();
        List<Point> starPoints = new LinkedList<>();
        for (MatOfPoint star : stars) {
            Moments moments = Imgproc.moments(star);
            starPoints.add(new Point(moments.get_m10() / moments.get_m00(), moments.get_m01() / moments.get_m00()));
        }
        filter.filterContoursOutput().forEach(MatOfPoint::release);
        filter.findContoursOutput().forEach(MatOfPoint::release);
        return starPoints;
    }

    @Override
    public Mat filterStars(Mat lightFrame, Mat blackFrame) {
        List<Point> stars = locateStars(lightFrame, blackFrame);
        Mat output = new Mat(lightFrame.size(), 0);
        for (Point star : stars) {
            Imgproc.rectangle(output, new Point(star.x - 2, star.y - 2), new Point(star.x + 2, star.y + 2), new Scalar(255, 255, 255));
        }
        return output;
    }
}
