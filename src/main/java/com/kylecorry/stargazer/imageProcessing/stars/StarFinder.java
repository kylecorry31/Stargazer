package com.kylecorry.stargazer.imageProcessing.stars;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarFinder {

    public Mat filterStars(Mat image, Mat blackImage) {
        Mat img = new Mat();
        Imgproc.cvtColor(image, img, Imgproc.COLOR_BGR2GRAY);
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble stdev = new MatOfDouble();
        Core.meanStdDev(img, mean, stdev);
        Mat dark = new Mat();
        Imgproc.cvtColor(blackImage, dark, Imgproc.COLOR_BGR2GRAY);
        Core.subtract(img, dark, img);
        double thresh = mean.get(0, 0)[0] + 2 * stdev.get(0, 0)[0];
        Imgproc.threshold(img, img, thresh, 255, Imgproc.THRESH_TOZERO);
        return img;
    }

    public Mat findStars(Mat image, Mat blackImage) {
        List<Point> stars = locateStars(image, blackImage);

        Mat output = new Mat(image.size(), 0);
        for (Point star : stars) {
            Imgproc.rectangle(output, new Point(star.x - 2, star.y - 2), new Point(star.x + 2, star.y + 2), new Scalar(255, 255, 255));
        }
        return output;
    }

    public List<Point> locateStars(Mat image, Mat blackImage) {
        Mat img = filterStars(image, blackImage);
        StarFilter2 filter = new StarFilter2();
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


    public PriorityQueue<StarPair> matchStars(List<Point> stars1, List<Point> stars2) { // O(n^2logn)
        PriorityQueue<StarPair> starPairs = new PriorityQueue<>(Comparator.comparingDouble(StarPair::getDistance));
        for (Point star : stars2) {
            starPairs.add(findClosest(star, stars1));
        }
        return starPairs;
    }

    private StarPair findClosest(Point star1, List<Point> stars) { // O(n)
        StarPair minPair = null;

        for (Point star : stars) {
            double dist = distance(star, star1);
            if (minPair == null) {
                minPair = new StarPair(star1, star, dist);
            } else if (dist < minPair.distance) {
                minPair = new StarPair(star1, star, dist);
            }
        }

        return minPair;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }


    public class StarPair {
        Point star1, star2;
        double distance;

        public StarPair(Point star1, Point star2, double distance) {
            this.star1 = star1;
            this.star2 = star2;
            this.distance = distance;
        }

        public Point getStar1() {
            return star1;
        }

        public Point getStar2() {
            return star2;
        }

        public double getDistance() {
            return distance;
        }
    }

}
