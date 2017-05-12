package com.kylecorry.imageEnhancement.imageProcessing;

import com.kylecorry.imageEnhancement.Main;
import com.kylecorry.imageEnhancement.storage.FileManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;

/**
 * Created by Kylec on 5/9/2017.
 */
class StarAligner {

    IntegerProperty imageNumber = new SimpleIntegerProperty(1, "imageNumber");
    private FileManager fileManager;

    public StarAligner(FileManager fileManager) {
        this.fileManager = fileManager;
    }


    Mat alignStars(List<String> files, Point a, Point aP, Point b, Point bP) {
        imageNumber.set(1);
        Point center = RotationMath.centerOfRotation(a, aP, b, bP);
        org.opencv.core.Point cvCenter = new org.opencv.core.Point(center.x, center.y);
        double totalAngleChange = RotationMath.angleBetween(center, a, aP);

        Mat current = fileManager.openImage(files.get(0));
        Mat average = Mat.zeros(current.size(), CvType.CV_32FC(3));
        Imgproc.accumulate(current, average);

        for (int i = 1; i < files.size(); i++) {
            imageNumber.set(i + 1);
            System.out.println("Aligning stars image " + (i + 1) + " of " + files.size());
            current = fileManager.openImage(files.get(i));

            double angle = -Math.toDegrees(totalAngleChange * i / (double) files.size());

            Mat rot = Imgproc.getRotationMatrix2D(cvCenter, angle, 1);
            Imgproc.warpAffine(current, current, rot, current.size());

            Imgproc.accumulate(current, average);
            current.release();
            rot.release();
        }

        current.release();
        Core.divide(average, Scalar.all(files.size()), average);
        return average;
    }


    private Point findClosest(Point point, List<Point> points) {
        double minDist = Double.MAX_VALUE;
        Point closest = null;
        for (Point p : points) {
            double dist = point.distance(p);
            if (dist <= minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }

}
