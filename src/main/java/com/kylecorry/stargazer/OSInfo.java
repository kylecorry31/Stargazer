package com.kylecorry.stargazer;

public class OSInfo {

    private ISystemProperties systemProperties;


    public OSInfo(ISystemProperties systemProperties){
        this.systemProperties = systemProperties;
    }

    public enum OS {
        LINUX,
        WINDOWS,
        MAC,
        OTHER
    }

    public enum Architecture {
        x64,
        x86,
        OTHER
    }

    public Architecture getArchitecture(){
        String arch = getArchitectureString();
        if(arch.contains("64")){
            return Architecture.x64;
        } else if(arch.contains("32") || arch.contains("86")){
            return Architecture.x86;
        } else {
            return Architecture.OTHER;
        }
    }

    private String getArchitectureString(){
        return systemProperties.getArchitecture();
    }


    public OS getOperatingSystem(){
        String osString = getOperatingSystemString();
        if(osString.contains("linux")){
            return OS.LINUX;
        } else if (osString.contains("mac") || osString.contains("darwin")){
            return OS.MAC;
        } else if (osString.contains("win")){
            return OS.WINDOWS;
        } else {
            return OS.OTHER;
        }
    }

    private String getOperatingSystemString(){
        return systemProperties.getOperatingSystem();
    }

}
