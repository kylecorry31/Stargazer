package com.kylecorry.stargazer.imageProcessing.stars;

import javafx.beans.property.IntegerProperty;
import org.opencv.core.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarAligner {

    private ProgressTrackableAligner aligner;

    public StarAligner(ProgressTrackableAligner aligner) {
        this.aligner = aligner;
    }

    public int getProgress() {
        return aligner.getProgress();
    }

    public IntegerProperty progressProperty() {
        return aligner.progressProperty();
    }

    public Mat align() {
        return aligner.align();
    }


//    Mat alignStars2(List<String> files, Mat blackFrame) {
//        imageNumber.set(1);
//        Mat current = fileManager.loadImage(files.get(0));
//        StarFinder finder = new StarFinder();
//        List<Point> lastStars = finder.locateStars(current, blackFrame);
//        Mat average = Mat.zeros(current.size(), CvType.CV_32FC(3));
//        Imgproc.accumulate(current, average);
//        double lastAngle = 0;
//        for (int i = 1; i < files.size(); i++) {
//            imageNumber.set(i + 1);
//            System.out.println("Aligning stars image " + (i + 1) + " of " + files.size());
//            current.release();
//            current = fileManager.loadImage(files.get(i));
//            List<Point> currentStars = finder.locateStars(current, blackFrame);
//            PriorityQueue<StarFinder.StarPair> stars = finder.matchStars(lastStars, currentStars);
//            StarFinder.StarPair first = stars.poll();
//            StarFinder.StarPair second = stars.poll();
//
//            Point cvCenter = RotationMath.centerOfRotation(first.getStar1(), first.getStar2(), second.getStar1(), second.getStar2());
//            double angle = lastAngle - Math.toDegrees(RotationMath.angleBetween(cvCenter, first.getStar1(), first.getStar2()));
//            Mat rot = Imgproc.getRotationMatrix2D(cvCenter, angle, 1);
//            Imgproc.warpAffine(current, current, rot, current.size());
//            lastStars = currentStars;
//            lastAngle = angle;
//            Imgproc.accumulate(current, average);
//            current.release();
//            rot.release();
//        }
//        current.release();
//        Core.divide(average, Scalar.all(files.size()), average);
//        return average;
//    }

}
