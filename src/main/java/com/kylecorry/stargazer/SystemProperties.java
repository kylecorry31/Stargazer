package com.kylecorry.stargazer;

public class SystemProperties implements ISystemProperties {

    private final static String OS_NAME_PROP = "os.name";
    private final static String OS_NAME_GENERIC_PROP = "generic";
    private final static String ARCH_PROP = "sun.arch.data.model";


    @Override
    public String getArchitecture() {
        return System.getProperty(ARCH_PROP);
    }

    @Override
    public String getOperatingSystem() {
        return System.getProperty(OS_NAME_PROP, OS_NAME_GENERIC_PROP).toLowerCase();
    }
}
