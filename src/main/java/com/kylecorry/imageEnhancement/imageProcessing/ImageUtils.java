package com.kylecorry.imageEnhancement.imageProcessing;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;

/**
 * Created by Kylec on 5/9/2017.
 */
public class ImageUtils {

    public static BufferedImage copyImage(BufferedImage source, int type) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), type);
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    // TODO: fix this mess
    public static Image toImage(Mat m) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", m, buffer);
        Image image = new Image(new ByteArrayInputStream(buffer.toArray()));
        buffer.release();
        return image;
    }

}
