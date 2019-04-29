package com.kylecorry.stargazer;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Kylec on 5/14/2017.
 */
public class OpenCVManager {

    private final static String LIB_NAME_LINUX = "libopencv_java345.so";
    private final static String LIB_NAME_WIN_64 = "opencv_java345_64.dll";
    private final static String LIB_NAME_WIN_32 = "opencv_java345_32.dll";

    private boolean isLoaded = false;

    public class OpenCVLoadException extends RuntimeException {
        public OpenCVLoadException() {
            super("Could not locate OpenCV libraries.");
        }
    }

    public class UnsupportedOperatingSystemException extends RuntimeException {
        public UnsupportedOperatingSystemException() {
            super("Your operating system is not supported.");
        }
    }

    public class UnsupportedArchitectureException extends RuntimeException {
        public UnsupportedArchitectureException() {
            super("Your hardware architecture is not supported.");
        }
    }

    private OpenCVManager() {
    }

    private static class OpenCVManagerHolder {
        public static final OpenCVManager instance = new OpenCVManager();
    }

    public static OpenCVManager getInstance() {
        return OpenCVManagerHolder.instance;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void load(ISystemProperties systemProperties) {
        OSInfo osInfo = new OSInfo(systemProperties);
        OSInfo.OS os = osInfo.getOperatingSystem();
        OSInfo.Architecture arch = osInfo.getArchitecture();
        String jarLocation = new File(OpenCVManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        String libName;
        if (os == OSInfo.OS.LINUX) {
            libName = LIB_NAME_LINUX;
        } else if (os == OSInfo.OS.WINDOWS) {
            if (arch == OSInfo.Architecture.x64) {
                libName = LIB_NAME_WIN_64;
            } else if (arch == OSInfo.Architecture.x86) {
                libName = LIB_NAME_WIN_32;
            } else {
                throw new UnsupportedArchitectureException();
            }
        } else {
            throw new UnsupportedOperatingSystemException();
        }
        boolean loaded = tryLoadingLibraries("../lib/" + libName, "libs/" + libName, jarLocation + "/" + libName);
        isLoaded = loaded;
        if (!loaded) {
            throw new OpenCVLoadException();
        }
    }

    private boolean tryLoadingLibraries(String... libraryPaths) {
        for (String path : libraryPaths) {
            if (tryLoadingLibrary(path)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryLoadingLibrary(String libraryPath) {
        try {
            System.load(new File(libraryPath).getAbsolutePath());
            return true;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

}
