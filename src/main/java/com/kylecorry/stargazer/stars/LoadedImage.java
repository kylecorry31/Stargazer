package com.kylecorry.stargazer.stars;

import org.opencv.core.Mat;

import java.util.Objects;

/**
 * An image which is already loaded into memory
 */
public class LoadedImage implements Image {

    private Mat image;

    /**
     * Default constructor
     * @param image the image
     */
    public LoadedImage(Mat image) {
        this.image = Objects.requireNonNull(image);
    }

    @Override
    public Mat getMat() {
        return image;
    }
}
