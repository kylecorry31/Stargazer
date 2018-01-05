package com.kylecorry.stargazer;

import org.junit.Test;

import static org.junit.Assert.*;

public class OpenCVManagerTest {

    @Test
    public void isLoaded() {
        OpenCVManager.getInstance().load(new SystemProperties());
        assertTrue(OpenCVManager.getInstance().isLoaded());
    }
}