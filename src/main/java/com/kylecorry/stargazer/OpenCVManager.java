package com.kylecorry.stargazer;

import java.io.File;

/**
 * Created by Kylec on 5/14/2017.
 */
public class OpenCVManager {

    public static void load() {
        String os = System.getProperty("os.name");

        if (os.equalsIgnoreCase("linux")) {
            try {
                System.load(new File("../lib/libopencv_java320.so").getAbsolutePath());
            } catch (UnsatisfiedLinkError e) {
                try {
                    System.load(new File("libs/libopencv_java320.so").getAbsolutePath());
                } catch (UnsatisfiedLinkError e1) {
                    System.err.println("Could not load opencv - FATAL");
                    System.exit(1);
                }
            }
        } else {

            try {
                System.load(new File("../lib/opencv_java320.dll").getAbsolutePath());
            } catch (UnsatisfiedLinkError e) {
                try {
                    System.load(new File("libs/opencv_java320.dll").getAbsolutePath());
                } catch (UnsatisfiedLinkError e1) {
                    System.err.println("Could not load opencv - FATAL");
                    System.exit(1);
                }
            }
        }
    }

}
