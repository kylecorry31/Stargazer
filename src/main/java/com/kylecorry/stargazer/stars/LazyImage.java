package com.kylecorry.stargazer.stars;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Objects;

/**
 * A class which only loads the image when getMat is called
 */
public class LazyImage implements Image {

    private Mat image;
    private String filename;

    /**
     * Default constructor
     * @param filename the filename of the image
     */
    public LazyImage(String filename) {
        this.filename = Objects.requireNonNull(filename);
    }


    @Override
    public Mat getMat() {
        if (image != null)
            return image;
        image = Imgcodecs.imread(filename);
        return image;
    }
}
