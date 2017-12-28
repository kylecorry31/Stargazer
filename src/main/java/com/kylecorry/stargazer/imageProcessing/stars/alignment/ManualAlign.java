package com.kylecorry.stargazer.imageProcessing.stars.alignment;

import com.kylecorry.stargazer.imageProcessing.RotationMath;
import com.kylecorry.stargazer.imageProcessing.stars.StarStreak;
import com.kylecorry.stargazer.storage.FileManager;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Kylec on 5/13/2017.
 */
public class ManualAlign extends ProgressTrackableAligner {

    private final StarStreak streak1;
    private final StarStreak streak2;
    private final FileManager fileManager;
    private final List<String> files;

    public ManualAlign(FileManager fileManager, List<String> files, StarStreak streak1, StarStreak streak2) {
        this.streak1 = streak1;
        this.streak2 = streak2;
        this.fileManager = fileManager;
        this.files = files;
    }

    @Override
    public Mat align() {
        setProgress(1);
        Point center = RotationMath.centerOfRotation(streak1.getStart(), streak1.getEnd(), streak2.getStart(), streak2.getEnd());
        org.opencv.core.Point cvCenter = new org.opencv.core.Point(center.x, center.y);
        double totalAngleChange = RotationMath.angleBetween(center, streak1.getStart(), streak1.getEnd());

        Mat current = fileManager.loadImage(files.get(0));
        Mat average = Mat.zeros(current.size(), CvType.CV_32FC(3));
        Imgproc.accumulate(current, average);

        for (int i = 1; i < files.size(); i++) {
            setProgress(i + 1);
            System.out.println("Aligning stars image " + (i + 1) + " of " + files.size());
            current = fileManager.loadImage(files.get(i));

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
}
