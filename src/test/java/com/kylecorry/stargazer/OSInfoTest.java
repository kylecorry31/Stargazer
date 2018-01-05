package com.kylecorry.stargazer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OSInfoTest {

    @Test
    public void testArchitecture(){
        MockSystemProperties properties = new MockSystemProperties("64", "x");
        OSInfo osInfo = new OSInfo(properties);
        assertEquals(OSInfo.Architecture.x64, osInfo.getArchitecture());
        properties.setArch("32");
        assertEquals(OSInfo.Architecture.x86, osInfo.getArchitecture());
        properties.setArch("86");
        assertEquals(OSInfo.Architecture.x86, osInfo.getArchitecture());
        properties.setArch("16");
        assertEquals(OSInfo.Architecture.OTHER, osInfo.getArchitecture());
    }

    @Test
    public void testOperatingSystem(){
        MockSystemProperties properties = new MockSystemProperties("64", "linux");
        OSInfo osInfo = new OSInfo(properties);
        assertEquals(OSInfo.OS.LINUX, osInfo.getOperatingSystem());
        properties.setOs("windows 10");
        assertEquals(OSInfo.OS.WINDOWS, osInfo.getOperatingSystem());
        properties.setOs("windows 8");
        assertEquals(OSInfo.OS.WINDOWS, osInfo.getOperatingSystem());
        properties.setOs("windows 7");
        assertEquals(OSInfo.OS.WINDOWS, osInfo.getOperatingSystem());
        properties.setOs("windows");
        assertEquals(OSInfo.OS.WINDOWS, osInfo.getOperatingSystem());
        properties.setOs("macos");
        assertEquals(OSInfo.OS.MAC, osInfo.getOperatingSystem());
        properties.setOs("darwin");
        assertEquals(OSInfo.OS.MAC, osInfo.getOperatingSystem());
        properties.setOs("something");
        assertEquals(OSInfo.OS.OTHER, osInfo.getOperatingSystem());
    }

}