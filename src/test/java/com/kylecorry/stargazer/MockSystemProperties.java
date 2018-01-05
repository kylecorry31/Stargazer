package com.kylecorry.stargazer;

public class MockSystemProperties implements ISystemProperties {

    private String arch;
    private String os;

    public MockSystemProperties(String arch, String os) {
        this.arch = arch;
        this.os = os;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public String getArchitecture() {
        return arch.toLowerCase();
    }

    @Override
    public String getOperatingSystem() {
        return os.toLowerCase();
    }
}
