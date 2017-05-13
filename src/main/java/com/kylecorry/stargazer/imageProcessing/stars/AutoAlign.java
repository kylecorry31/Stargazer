package com.kylecorry.stargazer.imageProcessing.stars;

import com.kylecorry.stargazer.storage.FileManager;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.util.List;

/**
 * Created by Kylec on 5/13/2017.
 */
public class AutoAlign extends ProgressTrackableAligner {

    private FileManager fileManager;
    private Mat blackFrame;
    private List<String> files;

    public AutoAlign(FileManager fileManager, List<String> files, Mat blackFrame) {
        this.fileManager = fileManager;
        this.blackFrame = blackFrame;
        this.files = files;
    }

    @Override
    public Mat align() {
        setProgress(1);
        Mat current = fileManager.loadImage(files.get(0));
        StarFinder finder = new StarFinder();
        Mat firstStarImage = finder.findStars(current, blackFrame);
        Mat average = Mat.zeros(current.size(), CvType.CV_32FC(3));
        Imgproc.accumulate(current, average);
        current.release();
        int warpMode = Video.MOTION_EUCLIDEAN;
        Mat warpMatrix = Mat.eye(2, 3, CvType.CV_32F);
        int converged = 1;
        for (int i = 1; i < files.size(); i++) {
            setProgress(i + 1);
            System.out.println("Aligning stars image " + (i + 1) + " of " + files.size());
            current = fileManager.loadImage(files.get(i));
            Mat currentStarImage = finder.findStars(current, blackFrame);
            try {
                Video.findTransformECC(currentStarImage, firstStarImage, warpMatrix, warpMode);
                Imgproc.warpAffine(current, current, warpMatrix, current.size());
                Imgproc.accumulate(current, average);
                converged++;
            } catch (Exception e) {
                System.err.println("Could not align");
            }
            currentStarImage.release();
            current.release();
        }
        firstStarImage.release();
        current.release();
        warpMatrix.release();
        Core.divide(average, Scalar.all(converged), average);
        return average;
    }

}
