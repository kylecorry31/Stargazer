package com.kylecorry.stargazer.stars;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Objects;

/**
 * A class which loads the image every time getMat is called
 */
public class OnTheFlyImage implements Image {

    private String filename;

    /**
     * Default constructor
     * @param filename the filename of the image
     */
    public OnTheFlyImage(String filename) {
        this.filename = Objects.requireNonNull(filename);
    }


    @Override
    public Mat getMat() {
        return Imgcodecs.imread(filename);
    }
}
