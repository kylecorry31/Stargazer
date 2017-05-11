package com.kylecorry.imageEnhancement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarAligner {

    public IntegerProperty imageNumber = new SimpleIntegerProperty(1, "imageNumber");


    public BufferedImage alignStars(List<String> files, Point a, Point aP, Point b, Point bP) {
        imageNumber.set(1);
        BufferedImage base = Main.getImage(files.get(0));

        Point center = RotationMath.centerOfRotation(a, aP, b, bP);
        double totalAngleChange = RotationMath.angleBetween(center, a, aP);

        Graphics2D graphics2D = base.createGraphics();
        AffineTransform oldTransform = graphics2D.getTransform();

        for (int i = 1; i < files.size(); i++) {
            imageNumber.set(i + 1);
            System.out.println("Aligning stars image " + (i+1) + " of " + files.size());
            BufferedImage image = Main.getImage(files.get(i));

            BufferedImage alphaSecond = ImageUtils.copyImage(image, BufferedImage.TYPE_INT_ARGB);
            float[] scales = {1f, 1f, 1f, (float) (1.0 / (i + 1))};
            float[] offsets = new float[4];
            RescaleOp rop = new RescaleOp(scales, offsets, null);


            double angle = totalAngleChange * i / (double) files.size();

            AffineTransform rotation = new AffineTransform();
            rotation.rotate(angle, center.x, center.y);

            graphics2D.transform(rotation);

            graphics2D.drawImage(alphaSecond, rop, 0, 0);

            graphics2D.setTransform(oldTransform);

        }

        graphics2D.dispose();

        return base;
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
