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

    private FilterSettings settings;
    private String stdevKey = "Standard deviations from mean";
    private double stdev = 2;
    private String upperKey = "Max brightness";
    private double upper = 255;


    public BackgroundSubtractionFilter(){
        settings = new FilterSettings();
        settings.put(stdevKey, new FilterSetting(stdevKey, stdev, 0, 255, "TODO"));
        settings.put(upperKey, new FilterSetting(upperKey, upper, 0, 255, "TODO"));
    }


    private Mat findStars(Mat lightFrame, Mat blackFrame) {
        Mat img = new Mat();
        Imgproc.cvtColor(lightFrame, img, Imgproc.COLOR_BGR2GRAY);
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble stdev = new MatOfDouble();
        if (blackFrame.channels() == 3 || blackFrame.channels() == 4) {
            Mat dark = new Mat();
            Imgproc.cvtColor(blackFrame, dark, Imgproc.COLOR_BGR2GRAY);
            Core.subtract(img, dark, img);
        }
        Core.meanStdDev(img, mean, stdev);
        double thresh = mean.get(0, 0)[0] + settings.get(stdevKey).getValue() * stdev.get(0, 0)[0];
        Imgproc.threshold(img, img, thresh, settings.get(upperKey).getValue(), Imgproc.THRESH_TOZERO);
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

    @Override
    public String getName() {
        return "Background Subtraction Filter";
    }

    @Override
    public FilterSettings getSettings() {
        return settings;
    }
}
