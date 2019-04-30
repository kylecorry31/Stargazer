package com.kylecorry.stargazer.stars;

import com.kylecorry.stargazer.imageProcessing.Averager;
import com.kylecorry.stargazer.imageProcessing.RotationMath;
import com.kylecorry.stargazer.imageProcessing.stars.StarStreak;
import com.kylecorry.stargazer.imageProcessing.stars.filters.IFilter;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class StreakAlignStarCombineStrategy implements StarCombineStrategy {

    private IFilter starFilter;

    public StreakAlignStarCombineStrategy(IFilter starFilter) {
        this.starFilter = Objects.requireNonNull(starFilter);
    }

    @Override
    public Image combine(List<Image> images, ProgressTrackerStrategy progressTrackerStrategy) {
        StarCombineStrategy streakProducer = StarCombineStrategy.lighten();
        Image lightened = streakProducer.combine(images, progress -> progressTrackerStrategy.setProgress(progress / 2));

        Mat filtered = starFilter.filterStars(lightened.getMat(), new Mat());
        lightened.getMat().release();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        int mode = Imgproc.RETR_EXTERNAL;
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(filtered, contours, hierarchy, mode, method);
        filtered.release();
        List<RotatedRect> boundaries = contours.stream().map((MatOfPoint points) -> Imgproc.minAreaRect(new MatOfPoint2f(points.toArray()))).collect(toList());
        boundaries.sort(Comparator.comparingDouble(rotatedRect -> -rotatedRect.boundingRect().area()));
        boundaries = boundaries.stream().filter(rotatedRect -> rotatedRect.boundingRect().area() > 200).limit(10).collect(toList());

        double centerX = 0;
        double centerY = 0;
        int count = 0;
        for (int i = 0; i < boundaries.size(); i++) {
            if (boundaries.get(i).angle != 0){
                Imgproc.drawMarker(lightened.getMat(), boundaries.get(i).boundingRect().tl(), new Scalar(0, 255, 0));
                Imgproc.rectangle(lightened.getMat(), boundaries.get(i).boundingRect().tl(), boundaries.get(i).boundingRect().br(), new Scalar(0, 0, 255));
            }
            StarStreak streak1 = new StarStreak(boundaries.get(i).boundingRect().tl(), boundaries.get(i).boundingRect().br());
            for (int j = 0; j < boundaries.size(); j++) {
                if (i == j)
                    continue;
                StarStreak streak2 = new StarStreak(boundaries.get(j).boundingRect().tl(), boundaries.get(j).boundingRect().br());
                Point center = RotationMath.centerOfRotation(streak1.getStart(), streak1.getEnd(), streak2.getStart(), streak2.getEnd());
                if (Double.isFinite(center.x) && Double.isFinite(center.y)) {
                    centerX += center.x;
                    centerY += center.y;
                    count++;
                }
            }
        }

        centerX /= count;
        centerY /= count;

        Point cvCenter = new Point(centerX, centerY);


        double totalAngleChange = 0;
        count = 0;
        for (RotatedRect boundary : boundaries) {
            totalAngleChange += RotationMath.angleBetween(cvCenter, boundary.boundingRect().tl(), boundary.boundingRect().br());
            count++;
        }


        totalAngleChange /= count;


        progressTrackerStrategy.setProgress(1.0 / (2 * images.size()) + 0.5);
        Mat current = images.get(0).getMat();
        int type = current.type();
        Averager averager = new Averager(current.size());
        averager.accumulate(current);
        current.release();
        for (int i = 1; i < images.size(); i++) {
            progressTrackerStrategy.setProgress((i + 1.0) / (2 * images.size()) + 0.5);
            current = images.get(i).getMat();
            double angle = Math.toDegrees(totalAngleChange * i / (double) images.size());
            Mat rot = Imgproc.getRotationMatrix2D(cvCenter, angle, 1);
            Imgproc.warpAffine(current, current, rot, current.size());
            averager.accumulate(current);
            current.release();
            rot.release();
        }
        Mat average = averager.getAverage(type);
        averager.release();
        return Image.fromMat(average);
    }
}
