package com.kylecorry.stargazer.stars;

import org.opencv.core.Mat;

public interface Image {

    /**
     * @return the image as an OpenCV Mat
     */
    Mat getMat();


    static Image fromMat(Mat mat){
        return new LoadedImage(mat);
    }

    static Image fromFilenameOnTheFly(String filename){
        return new OnTheFlyImage(filename);
    }

}
