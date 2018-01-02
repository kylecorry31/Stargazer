package com.kylecorry.stargazer;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Kylec on 5/14/2017.
 */
public class OpenCVManager {

    public static void load() {
        String os = System.getProperty("os.name");
        String arch = System.getProperty("sun.arch.data.model");
        String jarLocation = new File(OpenCVManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

        if (os.equalsIgnoreCase("linux")) {
            try {
                System.load(new File("../lib/libopencv_java340.so").getAbsolutePath());
            } catch (UnsatisfiedLinkError e) {
                try {
                    System.load(new File("libs/libopencv_java340.so").getAbsolutePath());
                } catch (UnsatisfiedLinkError e1) {
                    try {
                        System.load(jarLocation + "/libopencv_java340.so");
                    } catch(UnsatisfiedLinkError e2){
                        System.err.println("Could not load opencv - FATAL");
                        System.exit(1);
                    }
                }
            }
        } else {
            if (arch.equalsIgnoreCase("64")){
                try {
                    System.load(new File("../lib/opencv_java340_64.dll").getAbsolutePath());
                } catch (UnsatisfiedLinkError e) {
                    try {
                        System.load(new File("libs/opencv_java340_64.dll").getAbsolutePath());
                    } catch (UnsatisfiedLinkError e1) {
                        try {
                            System.load(jarLocation + "/opencv_java340_64.dll");
                        } catch(UnsatisfiedLinkError e2){
                            System.err.println("Could not load opencv - FATAL");
                            System.exit(1);
                        }
                    }
                }
            } else {
                try {
                    System.load(new File("../lib/opencv_java340_32.dll").getAbsolutePath());
                } catch (UnsatisfiedLinkError e) {
                    try {
                        System.load(new File("libs/opencv_java340_32.dll").getAbsolutePath());
                    } catch (UnsatisfiedLinkError e1) {
                        try {
                            System.load(jarLocation + "/opencv_java340_32.dll");
                        } catch(UnsatisfiedLinkError e2){
                            System.err.println("Could not load opencv - FATAL");
                            System.exit(1);
                        }
                    }
                }
            }

        }
    }

}
